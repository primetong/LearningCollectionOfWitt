# -*- coding: utf-8 -*-

import os
import re
import uuid
import shutil

from scrapy.cmdline import execute
from scrapy.utils.log import logging

def syncdir(src: str, dst: str, match=re.compile(r"(^[^_])|(\.py$)").search):
    # check params
    if not os.path.isdir(src):
        pass
    if not os.path.isdir(dst):
        os.mkdir(dst)
    
    # remove old files in dst
    tmp = os.path.join(dst, str(uuid.uuid4()))
    os.mkdir(tmp)
    for item in filter(match, os.listdir(dst)):
        shutil.move(os.path.join(dst, item), tmp)
    shutil.rmtree(tmp)

    # copy new files from src
    for item in filter(match, os.listdir(src)):
        lhs = os.path.join(src, item)
        rhs = os.path.join(dst, item)
        if os.path.isdir(lhs):
            syncdir(lhs, rhs, match)
        elif not os.path.isfile(rhs) or os.stat(lhs).st_mtime - os.stat(rhs).st_mtime > 1:
            shutil.copy(lhs, rhs)


if __name__ == "__main__":
    
    # settings
    project = "crawler"
    source  = project
    spider  = "BaiduImageSpider"
    flags   = [
        "-L", "INFO",
        "-a", "keyword=江泽民",
        "-a", "number=1000",
    ]

    # messages
    print("{}\n# Start {} - {}\n#".format(78*"#", project, spider))
    msg = "#  - {}"

    # get current directory
    src = os.path.dirname(os.path.realpath(__file__))
    dst = os.path.join(os.getcwd(), project)

    # create new project if not exists
    if not os.path.isdir(dst):
        print(msg.format("Create new scrapy project."))
        execute(["", "startproject", project])
        # exit

    # sync
    os.chdir(dst)
    lhs = os.path.join(src, source)
    rhs = os.path.join(dst, project)
    if os.path.isdir(lhs):
        print(msg.format("Copy source files."))
        syncdir(lhs, rhs)
    else:
        print(msg.format("Create source files."))
        syncdir(rhs, lhs)

    # add some settings
    cmd = [None, "crawl", spider] + flags

    # start the spider
    print(msg.format("Start Running."))
    logging.getLogger("scrapy").propagate = False
    execute(cmd)


# References:
#
# [ImportError: no module named win32api]
# (https://stackoverflow.com/a/35948588)
#
# [Scrapy: Pass arguments to cmdline.execute()]
# (https://stackoverflow.com/a/28354861)
# 
# [Find current directory and file's directory]
# (https://stackoverflow.com/a/5137509)
#
# [return string with first match Regex]
# (https://stackoverflow.com/a/38579883)
#
# [Regular expression operations]
# (https://docs.python.org/3/library/re.html)
#
# [How to delete a file or folder?]
# (https://stackoverflow.com/a/6996628)
#
# [Scrapy: Settings]
# (https://doc.scrapy.org/en/latest/topics/settings.html)
#
# [Scrapy: Logging]
# (https://doc.scrapy.org/en/latest/topics/logging.html)
#
# [How do I copy an entire directory of files into an existing directory using Python?]
# (https://stackoverflow.com/a/13814557)
#
# [How To Turn Off Logging in Scrapy (Python)]
# (https://stackoverflow.com/a/33204694)
#
# [Scrapy cmdline.execute stops script]
# (https://stackoverflow.com/a/30747158)