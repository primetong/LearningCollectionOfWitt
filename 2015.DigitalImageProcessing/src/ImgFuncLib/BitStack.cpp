#include "BitStack.hpp"
#include "BitStream.hpp"


BitStack::
BitStack()
	: _Idx(0U)
	, _Buffer(1)
{}

void BitStack::
push(bool is_one)
{
	if (is_one)
		_Buffer.back() |= 1U << _Idx;
	else 
		_Buffer.back() &= ~(1U << _Idx);

	if (++_Idx >= BITS_PER_UINT) {
		_Buffer.emplace_back();
		_Idx = 0;
	}
}

void BitStack::
pop()
{
	if (_Buffer.size() == 1U && _Idx == 0U)
		throw std::runtime_error("cannot pop an empty stack.");

	if (_Idx == 0U) {
		_Idx = BITS_PER_UINT;
		_Buffer.pop_back();
	}

	_Idx --;
	_Buffer.back() &= ~(1U << _Idx);
}

bool BitStack::
top() const
{
	if (_Buffer.size() == 1U && _Idx == 0U)
		throw std::runtime_error("cannot get element from an empty stack.");

	return static_cast<bool>((_Buffer.back() >> (_Idx - 1U)) & 1U);
}

void BitStack::
clear()
{
	_Idx = 0U;
	_Buffer.clear();
	_Buffer.emplace_back();
}

size_t BitStack::
size() const
{
	return (_Buffer.size() - 1U) << 3U | _Idx;
}

BitStream & operator << (BitStream & lhs, BitStack const & rhs) 
{
	auto & bits = rhs._Buffer;

	for (auto i = 0U, n = bits.size() - 1U; i < n; i++)
		lhs << bits[i];

	auto back = bits.back();
	for (auto i = 0U, n = rhs._Idx; i < n; i++)
		lhs << static_cast<bool>((back >> i) & 1U);

	return lhs;
}

