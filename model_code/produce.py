# -*- coding: utf-8 -*-
import torch
import torch.nn as nn
from torchvision import models
from PIL import Image
import numpy as np
import matplotlib.pyplot as plt
import torchvision.transforms as transforms

def predict(image_path):
    # 定义数据预处理
    transform = transforms.Compose([
        transforms.Resize((512, 384)),
        transforms.ToTensor(),
    ])

    # 加载测试集图片
    test_image_path = image_path
    test_image = Image.open(test_image_path)
    test_image = transform(test_image).unsqueeze(0)  # 增加一个维度以符合模型输入

    # 加载模型
    model = models.resnet18(pretrained=False)  # 注意此处的模型结构应与保存时相同
    num_ftrs = model.fc.in_features
    model.fc = nn.Linear(num_ftrs, 6)  # 输出层6个类别，与保存时相同
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    model.load_state_dict(torch.load("trash_classification_model.pth", map_location=device))
    model = model.to(device)

    # 模型预测
    model.eval()
    with torch.no_grad():
        test_image = test_image.to(device)
        outputs = model(test_image)
        _, predicted = torch.max(outputs, 1)
        predicted_label = predicted.item()

    return predicted_label
    # 加载标签
    # class_labels = ["glass", "paper", "cardboard", "plastic", "metal", "trash"]
    # predicted_class = class_labels[predicted_label]

    # # 展示预测结果
    # plt.imshow(np.array(test_image.squeeze(0).cpu().permute(1, 2, 0)))
    # plt.title(f"Predicted Class: {predicted_class}")
    # plt.show()
    #
    # print(f"该垃圾是{predicted_class}")
