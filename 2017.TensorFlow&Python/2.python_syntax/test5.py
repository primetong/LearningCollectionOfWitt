def get_animal(index):
	animals = ['dog', 'cat', 'ant']
	if (index < 0 or index >= len(animals)):
		return -1
	animal = animals[index]
	return animal

if __name__=='__main__':
	index = input('please input index:')
	animal = get_animal(index)
	print animal
