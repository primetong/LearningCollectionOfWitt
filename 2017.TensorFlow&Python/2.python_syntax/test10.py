import os
a = os.path.abspath('.')
for filename in os.listdir(a):
	print filename
