# -*- coding: utf-8 -*-

import os
import time
import scrapy
import twisted

from selenium import webdriver
from crawler.items import ImageItem

class BaiduImageSpider(scrapy.Spider):
    name = "BaiduImageSpider"
    allowed_domains = ["baidu.com"]
    download_delay = 1

    def __init__(self, keyword="lena", number="100", *args, **kwargs):
        super(BaiduImageSpider, self).__init__(*args, **kwargs)
        # init arguments
        self.keyword = keyword
        self.number = int(number)
        # init webdriver
        path = os.path.join("tools", "phantomjs")
        self.driver = webdriver.PhantomJS(path)

    def start_requests(self):
        urls = [
            r"https://image.baidu.com/search/index?tn=baiduimage&word={}"\
            .format(self.keyword),
        ]
        for url in urls:
            yield scrapy.Request(url = url, callback = self.parse, errback = self.error)

    def parse(self, response):
        self.logger.info("start parsing")
        self.driver.get(response.url)
        
        # find n images
        xpath = r'//*[@id="imgid"]/div[*]/ul/li[*]/div[*]/a/img[@data-imgurl]'
        n = self.number
        i = 0
        while i < n:
            # count images
            elems = self.driver.find_elements_by_xpath(xpath)

            # update i
            if i != len(elems): i = len(elems)
            else: break

            # scroll down
            actions = webdriver.ActionChains(self.driver)
            actions.move_to_element(elems[-1])
            actions.perform()
            time.sleep(0.5)

        self.logger.info("found {} image(s)".format(i))
        
        # save images
        item = ImageItem()
        if elems:
            item["image_urls"] = [elem.get_attribute("data-imgurl") for elem in elems]
            item["images"] = ["{}{:04}{}".format(self.keyword, i, os.path.splitext(x)[1])\
                for i,x in enumerate(item["image_urls"])]
            self.logger.info("start downloading")
            return item

    # errors from downloader
    def error(self, failure):
        self.logger.error(repr(failure))


# References:
#
# [Scrapy: Logging]
# (https://doc.scrapy.org/en/latest/topics/logging.html)
# [PhantomJS]
# (http://phantomjs.org/download.html)
# [selenium with scrapy for dynamic page]
# (https://stackoverflow.com/a/17979285)
# [XPath Syntax]
# (https://www.w3schools.com/xml/xpath_syntax.asp)
# [Scroll down to bottom of infinite page with PhantomJS in Python]
# (https://stackoverflow.com/a/28928684)
# [Scrapy: Spider arguments]
# (https://doc.scrapy.org/en/latest/topics/spiders.html#spider-arguments)
# [Scrapy: Using errbacks to catch exceptions in request processing]
# (https://doc.scrapy.org/en/latest/topics/request-response.html#topics-request-response-ref-errbacks)
# [Crawling with Scrapy – Download Images]
# (http://www.scrapingauthority.com/scrapy-download-images/)
# [scrapy-3 利用框架，抓取图片]
# (https://my.oschina.net/jastme/blog/280114)
# [Getting index of item while processing a list using map in python]
# (https://stackoverflow.com/a/5432789)