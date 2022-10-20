import pandas as pd
import nltk
from nltk.tokenize import RegexpTokenizer
import string
import re
import numpy as np
import time
import csv
start_time = time.time()
df = pd.read_csv(r"C:\Users\chatzichristodoulou\PycharmProjects\pythonProject1\output1.csv",header=0)

print("-2")

df = df[~df['roles'].str.contains("βουλη", na=False)]
df = df[~df['roles'].str.contains("α αντιπροεδρος|β αντιπροεδρος|γ αντιπροεδρος|δ αντιπροεδρος|ε αντιπροεδρος|στ αντιπροεδρος|ζ αντιπροεδρος|η αντιπροεδρος", na=False)]
print("-1")
def add_whitespace(sentence):
    sentence = re.sub(r'(?<=[.,])(?=[^\s])', r' ',sentence)
    return sentence

df['speech'] = df['speech'].apply(add_whitespace)

print("0")

df = df.dropna(axis=0, subset=['member_name'])
df = df.dropna(axis=0, subset=['roles'])
df = df.dropna(axis=0, subset=['speech'])
df = df.dropna(axis=0, subset=['sitting_date'])
df['speech'] = df['speech'].str.lower()
df['speech'] = df['speech'].apply(str).str.replace('[^\w\s]','')
df['speech'] = df['speech'].str.replace('\d+', '')
df['sitting_date'] = df['sitting_date'].str[6:]

print("1")

df['speech'] = df.apply(lambda row: nltk.word_tokenize(row['speech']), axis=1)


lista = pd.read_csv(r'C:\Users\chatzichristodoulou\PycharmProjects\pythonProject\stopwords-el.csv', header=None);
test = lista.values.tolist()
flat_list = [item for sublist in test for item in sublist]
df['speech'] = df['speech'].apply(lambda x: ([word for word in x if word not in (flat_list)]))

print("2")

def remove_empty(x):
    if type(x) is str:
        x = x.split(",")
        x = [ y for y in x if y.strip()]
        return ",".join(x)
    elif type(x) is list:
        return [ y for y in x if y.strip()]

df['speech'] = df['speech'].apply(remove_empty)
df.columns = df.columns.str.replace(' ', '')
df = df.dropna(axis=0, subset=['speech'])
df['speech'] = df['speech'].apply(remove_empty)

print("3")

def list_string(words_list):
    return ' '.join(f'{word}' for word in words_list)
df['speech'] = df['speech'].apply(list_string)
df.columns = df.columns.str.replace(' ', '')
df['speech'] = df['speech'].apply(remove_empty)
df = df.dropna(axis=0, subset=['speech'])
df['speech']=df['speech'].replace(r'^\s*$', np.nan, regex=True)
df = df.dropna(axis=0, subset=['speech'])

print("4")

df.to_csv("final",encoding="utf-8")
print("--- %s seconds ---" % (time.time() - start_time))


