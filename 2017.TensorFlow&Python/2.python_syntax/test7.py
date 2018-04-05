class Animal():
	def __init__(self, name, age):
		self.name = name
		self.age = age
	def get_animal(self):
		print(self.name, self.age)
if __name__=='__main__':
	ani = Animal('dog', 16)
	ani.get_animal()
