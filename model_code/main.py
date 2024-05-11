# -*- coding: utf-8 -*-
from produce import predict
import sys
def main():
    path=sys.argv[1]
    print(path)
    imagePath = "model_code/images/"+path # 测试路径
    result = predict(imagePath)
    print(result)
    return result
if __name__ == "__main__":
    main()
