/*********************************************************************
 *	@file		BitStack.hpp
 *	@brief		
 *********************************************************************/
#pragma once

#include <iostream>
#include <vector>

class BitStream;

class BitStack
{
public:
	BitStack();

public:

	void push(bool one);

	void pop();

	bool top() const;

	void clear();

	size_t size() const;

	void dump() const 
	{
		for (auto i = 0U, n = _Buffer.size() - 1U; i < n; i++) {
			auto elem = _Buffer[i];
			for (auto x = 0U; x < BITS_PER_UINT; x++)
				std::cout << ((elem >> x) & 1U);
		}

		auto elem = _Buffer.back();
		for (auto x = 0U; x < _Idx; x++)
			std::cout << ((elem >> x) & 1U);
	}

	friend BitStream & operator << (BitStream & lhs, BitStack const & rhs);

private:
	
	typedef size_t element;
	static const size_t BITS_PER_UINT = sizeof(element) << 3U;

private:

	size_t _Idx;
	std::vector<element> _Buffer;
};



