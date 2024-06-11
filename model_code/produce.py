import torch
import torch.nn as nn
from torchvision import transforms, models
from PIL import Image
import joblib
from flask import Flask, request, jsonify

torch.hub.set_dir('./modelCache')
# 定义特征提取器
class FeatureExtractor(nn.Module):
    def __init__(self, model):
        super(FeatureExtractor, self).__init__()
        self.features = nn.Sequential(*list(model.children())[:-1])

    def forward(self, x):
        x = self.features(x)
        x = torch.flatten(x, 1)
        return x

# 使用预训练的ResNet50
model = models.resnet50(pretrained=True)
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


# 创建Flask应用
app = Flask(__name__)


@app.route('/', methods=['POST'])
def classify():
    data = request.json
    if 'imagePath' not in data:
        return jsonify({'error': 'No imagePath provided'}), 400

    imagePath = data['imagePath']
    try:
        prediction = classify_image(imagePath, random_forest)
        return jsonify({'prediction': int(prediction)}), 200
    except Exception as e:
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
