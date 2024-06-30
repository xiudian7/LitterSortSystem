import json
import os

from flask import Flask, request, jsonify
from modelscope.pipelines import pipeline
from modelscope.utils.constant import Tasks

# 设置自定义的缓存目录
os.environ['MODELSCOPE_CACHE'] = './modelCache'

# 使用 pipeline 加载模型
image_classification = pipeline(Tasks.image_classification,
                                model='damo/cv_convnext-base_image-classification_garbage')

# 创建 Flask 应用
app = Flask(__name__)

@app.route('/', methods=['POST'])
def classify():
    data = request.json
    if 'imagePath' not in data:
        return jsonify({'error': 'No imagePath provided'}), 400

    imagePath = data['imagePath']
    try:
        result = image_classification(imagePath) # 返回字典类型
        res = result['labels'][0]
        return jsonify({'prediction': str(res)}), 200  # 返回预测结果的JSON 格式
    except Exception as e:
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
