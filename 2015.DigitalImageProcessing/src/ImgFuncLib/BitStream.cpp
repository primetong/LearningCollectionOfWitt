#include "BitStream.hpp"



BitStream::
BitStream()
	: _IsGood(true)
	, _BeginIdx(0U)
	, _EndIdx(0U)
	, _Buffer(1)
{}

BitStream::
operator bool() const
{
	/*
	 * ²Î¿¼×ÊÁÏ£º
	 * [if (cin >> x) - Why can you use that condition?]
	 * (http://stackoverflow.com/questions/6791520/if-cin-x-why-can-you-use-that-condition)
	 */
	
	return _IsGood;
}

bool BitStream::
eof() const
{
	auto size = _Buffer.size();
	return (!size) || (size == 1U && _BeginIdx >= _EndIdx);
}

size_t BitStream::
size() const
{
	return size_t(((_Buffer.size() - 1U) << LOG_BITS_PER_UINT) + _EndIdx - _BeginIdx);
}

void BitStream::
clear()
{
	_Buffer.clear();
	_Buffer.emplace_back();
	_BeginIdx = 0U;
	_EndIdx = 0U;
}

