import argparse
import torch
import torch.nn as nn
from torchvision import transforms, models
from PIL import Image
import joblib
import numpy as np

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

# print("特征提取器加载完成")

# 数据转换
transform = transforms.Compose([
    transforms.Resize((512, 384)),
    transforms.ToTensor(),
])

# 加载保存的模型
random_forest = joblib.load('best_random_forest.pkl')

# print("模型加载完成")

def classify_image(image_path, model):
    image = Image.open(image_path)
    image = transform(image)
    image = image.unsqueeze(0).to(device)

    with torch.no_grad():
        feature = feature_extractor(image).cpu().numpy()

    prediction = model.predict(feature)
    return prediction[0]

if __name__ == "__main__":
    # 创建命令行参数解析器
    parser = argparse.ArgumentParser(description='图像分类')
    parser.add_argument('image_path', type=str, help='要分类的图像路径')

    # 解析命令行参数
    args = parser.parse_args()

    # 使用随机森林分类器进行分类
    rf_prediction = classify_image(args.image_path, random_forest)
    # print(f"随机森林分类器预测结果: {rf_prediction}")
    print(rf_prediction)