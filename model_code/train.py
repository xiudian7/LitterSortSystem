import torch
import torch.nn as nn
from torchvision import transforms, models
from torch.utils.data import DataLoader
from PIL import Image
import pandas as pd
import numpy as np
from sklearn.tree import DecisionTreeClassifier
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import accuracy_score
import joblib

# 定义数据集类
class TrashDataset(torch.utils.data.Dataset):
    def __init__(self, txt_file, root_dir, transform=None):
        self.annotations = pd.read_csv(txt_file, delimiter=' ', header=None)
        self.root_dir = root_dir
        self.transform = transform

    def __len__(self):
        return len(self.annotations)

    def __getitem__(self, idx):
        img_name = self.annotations.iloc[idx, 0]
        img_path = f"{self.root_dir}/{img_name}"
        image = Image.open(img_path)
        label = int(self.annotations.iloc[idx, 1]) - 1
        if self.transform:
            image = self.transform(image)
        return image, label

# 数据转换
transform = transforms.Compose([
    transforms.Resize((512, 384)),
    transforms.ToTensor(),
])

# 数据集路径和根目录
train_data = "./archive/one-indexed-files-notrash_train.txt"
val_data = "./archive/one-indexed-files-notrash_val.txt"
test_data = "./archive/one-indexed-files-notrash_test.txt"
root_dir = "./archive/Garbage classification"

# 加载数据集
train_dataset = TrashDataset(txt_file=train_data, root_dir=root_dir, transform=transform)
val_dataset = TrashDataset(txt_file=val_data, root_dir=root_dir, transform=transform)
test_dataset = TrashDataset(txt_file=test_data, root_dir=root_dir, transform=transform)

# 创建数据加载器
batch_size = 16
train_loader = DataLoader(train_dataset, batch_size=batch_size, shuffle=True)
val_loader = DataLoader(val_dataset, batch_size=batch_size, shuffle=False)
test_loader = DataLoader(test_dataset, batch_size=batch_size, shuffle=False)

print("数据加载完成")

# 定义特征提取器
class FeatureExtractor(nn.Module):
    def __init__(self, model):
        super(FeatureExtractor, self).__init__()
        self.features = nn.Sequential(*list(model.children())[:-1])

    def forward(self, x):
        x = self.features(x)
        x = torch.flatten(x, 1)
        return x

# 使用预训练的ResNet18
model = models.resnet18(pretrained=True)
feature_extractor = FeatureExtractor(model)
feature_extractor.eval()

# 使用GPU
device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
feature_extractor = feature_extractor.to(device)

print("特征提取器加载完成")

# 将数据集加载到特征提取器中
def extract_features(data_loader):
    features = []
    labels = []
    with torch.no_grad():
        for images, label in data_loader:
            images = images.to(device)
            output = feature_extractor(images)
            features.append(output.cpu().numpy())
            labels.append(label.numpy())
    features = np.vstack(features)
    labels = np.concatenate(labels)
    return features, labels

# 定义训练和验证函数
def train_and_evaluate(epochs):
    best_val_accuracy = 0.0
    for epoch in range(epochs):
        print(f"Epoch {epoch + 1}/{epochs}")

        # 提取训练、验证和测试集的特征
        print("开始提取训练集特征...")
        train_features, train_labels = extract_features(train_loader)
        print("训练集特征提取完成")

        print("开始提取验证集特征...")
        val_features, val_labels = extract_features(val_loader)
        print("验证集特征提取完成")

        print("开始提取测试集特征...")
        test_features, test_labels = extract_features(test_loader)
        print("测试集特征提取完成")

        # 训练随机森林分类器
        print("开始训练随机森林分类器...")
        random_forest = RandomForestClassifier(n_estimators=100)
        random_forest.fit(train_features, train_labels)
        print("随机森林分类器训练完成")

        # 验证随机森林分类器
        print("开始验证随机森林分类器...")
        val_preds_rf = random_forest.predict(val_features)
        val_accuracy_rf = accuracy_score(val_labels, val_preds_rf)
        print(f"随机森林验证集准确率: {val_accuracy_rf * 100:.2f}%")

        if val_accuracy_rf > best_val_accuracy:
            best_val_accuracy = val_accuracy_rf
            best_random_forest = random_forest

    # 保存最好的随机森林分类器
    joblib.dump(best_random_forest, 'best_random_forest.pkl')
    print("最好的随机森林分类器已保存")

    # 测试最好的随机森林分类器
    print("开始测试最好的随机森林分类器...")
    test_preds_rf = best_random_forest.predict(test_features)
    test_accuracy_rf = accuracy_score(test_labels, test_preds_rf)
    print(f"随机森林测试集准确率: {test_accuracy_rf * 100:.2f}%")

# 训练和评估模型
epochs = 5
train_and_evaluate(epochs)