import pandas as pd
import platform
import time
start = time.time()
print(platform.architecture()[0])
df = pd.read_csv(r'C:\Users\Χάρης\PycharmProjects\pythonProject11\output2.csv',header=0)
print('read the file')
print(df.head())
print(df.info())
end = time.time()
print(end-start)
df = df.dropna(axis=0, subset=['member_name'])
df['speech']=df['speech'].str.lower()
def remove_whitespace(text):
    return " ".join(text.split())
df['speech']=df['speech'].apply(remove_whitespace)
print('removed whitespace')
end = time.time()
print(end-start)
import spacy
from spacy.lang.el.examples import sentences
#!py -m spacy download el_core_web_lg
nlp = spacy.load("el_core_news_lg")
print('read the elcorenewslg')
end = time.time()
print(end-start)
def lemlist (text):

    list = []
    doc = nlp(text)
    for token in doc:
        list.append(token.lemma_)
    return list
df['speech'] = df['speech'].apply(lemlist)
print('aplied lem')
end = time.time()
print(end-start)
df.to_csv("output2.csv", encoding = "utf-8")
end = time.time()
print(end-start)