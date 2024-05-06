# -*- coding: utf-8 -*-
import torch
import torch.nn as nn
import torch.optim as optim
from torch.utils.data import DataLoader
from torchvision import transforms, models
from PIL import Image
import pandas as pd
import matplotlib.pyplot as plt


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

# 定义模型
model = models.resnet18(pretrained=True)
num_ftrs = model.fc.in_features
model.fc = nn.Linear(num_ftrs, 6)  # 输出层6个类别
device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
model = model.to(device)

# 定义损失函数和优化器
criterion = nn.CrossEntropyLoss()
optimizer = optim.SGD(model.parameters(), lr=0.001, momentum=0.9)

# 训练模型
num_epochs = 10
for epoch in range(num_epochs):
    model.train()
    running_loss = 0.0
    for inputs, labels in train_loader:
        inputs, labels = inputs.to(device), labels.to(device)
        optimizer.zero_grad()
        outputs = model(inputs)
        loss = criterion(outputs, labels)
        loss.backward()
        optimizer.step()
        running_loss += loss.item() * inputs.size(0)
    epoch_loss = running_loss / len(train_dataset)
    print(f"Epoch {epoch + 1}/{num_epochs} Training Loss: {epoch_loss:.4f}")

    # 在验证集上评估模型
    model.eval()
    val_correct = 0
    val_total = 0
    with torch.no_grad():
        for inputs, labels in val_loader:
            inputs, labels = inputs.to(device), labels.to(device)
            outputs = model(inputs)
            _, predicted = torch.max(outputs, 1)
            val_correct += (predicted == labels).sum().item()
            val_total += labels.size(0)
    val_accuracy = val_correct / val_total
    print(f"Epoch {epoch + 1}/{num_epochs} Validation Accuracy: {val_accuracy:.4f}")

# 保存训练的模型
torch.save(model.state_dict(), "trash_classification_model.pth")
print("Model Saved")

# 在测试集上评估模型并展示图片预测结果
model.eval()
test_correct = 0
test_total = 0
with torch.no_grad():
    for inputs, labels in test_loader:
        inputs, labels = inputs.to(device), labels.to(device)
        outputs = model(inputs)
        _, predicted = torch.max(outputs, 1)
        test_correct += (predicted == labels).sum().item()
        test_total += labels.size(0)

        # 展示部分图片的预测结果
        for i in range(10):
            image = inputs[i].cpu().numpy().transpose((1, 2, 0))  # 将图片从 Tensor 转为 NumPy 数组，并转置通道
            label = labels[i].cpu().item()
            pred = predicted[i].cpu().item()
            plt.imshow(image)
            plt.title(f"True: {label}, Predicted: {pred}")
            plt.show()

test_accuracy = test_correct / test_total
print(f"Test Accuracy: {test_accuracy:.4f}")
