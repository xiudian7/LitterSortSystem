# -*- coding: utf-8 -*-
from produce import predict
def main():
    imagePath = "model_code/paper162.jpg" # 测试路径
    result = predict(imagePath)
    print(result)
    return result
if __name__ == "__main__":
    main()
