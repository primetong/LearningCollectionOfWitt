# -*- coding: utf-8 -*-

# Define here the models for your scraped items
#
# See documentation in:
# http://doc.scrapy.org/en/latest/topics/items.html

import scrapy

# My Item
class ImageItem(scrapy.Item):
    image_urls = scrapy.Field()
    images = scrapy.Field()


# References:
#
# [Downloading and processing files and images - Usage example]
# (https://doc.scrapy.org/en/1.4/topics/media-pipeline.html?highlight=images#usage-example)
