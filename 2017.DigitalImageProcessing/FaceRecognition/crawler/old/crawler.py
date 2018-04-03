#coding:UTF-8

#Lib
#
#[Beautiful Soup 4]
#(https://www.crummy.com/software/BeautifulSoup/bs4/doc/index.zh.html)
#[Requests]
#(http://docs.python-requests.org/en/master/)
#[lxml]
#(http://lxml.de/)
#[selenium]
#(http://selenium-python.readthedocs.io/index.html)

import os
import bs4
import urllib
import requests
from selenium import webdriver

class UrlCorrector:

    def __init__(self):
        pass

    def __init__(self, url : str):
        self.set_default(url)

    def set_default(self, url : str):
        scheme, netloc, *_3 = urllib.parse.urlparse(url)
        self.__scheme__ = scheme
        self.__netloc__ = netloc

    def url(self, url : str):
        scheme, netloc, *_3 = urllib.parse.urlparse(url)
        if not scheme: scheme = self.__scheme__
        if not netloc: netloc = self.__netloc__
        url = urllib.parse.urlunparse((scheme, netloc, *_3))
        return url

    def res(self, url : str):
        scheme, netloc, path, *_4 = urllib.parse.urlparse(url)
        if not scheme: scheme = self.__scheme__
        if not netloc: netloc = self.__netloc__
        link = urllib.parse.urlunparse((scheme, netloc, path, *_4))
        name = path[path.rfind('/') + 1:]
        type = name[name.rfind('.'):]
        return (link, name, type)


class Site:
    url = ""
    payload = {}
    headers = {
        'User-Agent':'Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.48'
    }

if __name__ == "__main__":

    sogou = Site()
    sogou.url = r"http://pic.sogou.com/pics"
    sogou.payload = { "query": "lena" }

    baidu = Site()
    baidu.url = r"https://image.baidu.com/search/index"
    baidu.payload = { "tn": "baiduimage", "word" : "lena"}

    test = Site()
    test.url = r"http://www.bilibili.com"

    dir = os.path.join("crawler")

    site = test

    with requests.session() as sess:
        sess.params .update(site.payload)
        sess.headers.update(site.headers)

        try:
            resp = sess.get(site.url, timeout=4.)
        except requests.ReadTimeout:
            print("Access: timeout")
            exit()

        if resp.status_code != 200:
            print("Access:", resp.status_code)
            exit()
        else:
            print("Access:", resp.url, "\n")

        soup = bs4.BeautifulSoup(resp.text, "lxml")
        imgs = soup.find_all("img")

        if not imgs:
            print("No Images")
            exit()

        if not os.path.exists(dir):
            os.makedirs(dir)

        msg = "GET [{0:^6}]: {1}\n" + 14*" " + "{2}"

        corr = UrlCorrector(site.url)
        for img in imgs:
            if not img.has_attr("src") : continue

            link, name, ext = corr.res(img["src"])
            if not link or not name:
                print(msg.format("Failed", link, name))
                continue

            try:
                resp = sess.get(link, timeout=4.)
            except requests.ReadTimeout:
                print(msg.format("Failed", link, "timeout"))
                continue

            if resp.status_code != 200:
                print(msg.format("Failed", link, resp.status_code))
                continue

            name = os.path.join(dir, name)
            with open(name, 'wb') as f:
                f.write(resp.content)
                print(msg.format("Done", link, name))

# note:
# 
# [Best way to check if a list is empty]
# (https://stackoverflow.com/a/53522)
# [AttributeError: 'module' object has no attribute 'webdriver']
# (https://stackoverflow.com/q/37801823)
