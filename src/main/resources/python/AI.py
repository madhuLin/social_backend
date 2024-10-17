import requests
import pandas as pd
import re
import os
import openai
import sys

# 设置 API 密钥
api_key = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"

# 确保设置了 API 密钥
openai.api_key = api_key

# 从标准输入中读取文本
text = sys.stdin.read().strip()

# 初始请求获取公司名称
response = openai.ChatCompletion.create(
    model="gpt-3.5-turbo",
    messages=[
        {
            "role": "user",
            "content": text
            + "請列出所有的公司中文名稱。並只要返回公司名稱就好不要有其他多餘的話",
        }
    ],
)

# 解析 GPT 返回的公司名稱
text_gpt = response.choices[0].message["content"]
company_names = re.findall(r"[\u4e00-\u9fff]+", text_gpt)

if company_names:
    all_data = []
    for Company_Name in company_names:
        url = "https://data.gcis.nat.gov.tw/od/data/api/6BBA2268-1367-4B42-9CCA-BC17499EBE8C"
        params = {
            "$format": "json",
            "$filter": f"Company_Name like '{Company_Name}' and Company_Status eq '01'",
            "$skip": 0,
            "$top": 50,
        }

        response = requests.get(url, params=params)

        if response.status_code == 200:
            data1 = response.json()
            df = pd.DataFrame(data1)
            Company_Accounting = (
                df["Business_Accounting_NO"].iloc[0] if not df.empty else None
            )
            if Company_Accounting:
                url = "https://data.gcis.nat.gov.tw/od/data/api/236EE382-4942-41A9-BD03-CA0709025E7C"
                params = {
                    "$format": "json",
                    "$filter": f"Business_Accounting_NO eq {Company_Accounting}",
                    "$skip": 0,
                    "$top": 50,
                }

                response = requests.get(url, params=params)

                if response.status_code == 200:
                    data2 = response.json()
                    all_data.append(
                        {"company": Company_Name, "data1": data1, "data2": data2}
                    )
                else:
                    print("Failed to retrieve data:", response.status_code)
            else:
                print(f"未找到 {Company_Name} 的會計編號")
        else:
            print("Failed to retrieve data:", response.status_code)

    if all_data:
        combined_data_str = " ".join([str(company_data) for company_data in all_data])
        msg = f"{combined_data_str} 根據這些資料，幫我分辨{text}這篇文章的可信度1~100分。並只要返回評分就好，不要說出你的意見，不要對我說出你的評論。"

        # GPT-4 API 呼叫
        response = openai.ChatCompletion.create(
            model="gpt-4-turbo",
            messages=[
                {
                    "role": "user",
                    "content": msg,
                }
            ],
        )

        print("文章可信度評分:", response.choices[0].message["content"])
    else:
        print("未找到公司資料，無法進行評分")
