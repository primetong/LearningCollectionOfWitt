/*********************************************************************
 *	@file		BitStream.hpp
 *	@brief		
 *********************************************************************/
#pragma once

#include <deque>
#include <iostream>

class BitStream
{
public:

	BitStream();

public:

	template<typename T> BitStream & operator << (T rhs);

	template<typename T> BitStream & operator >> (T & rhs);

	explicit operator bool() const;

	bool eof() const;

	size_t size() const;

	void dump() const 
	{
		if (_Buffer.empty())
			return;

		if (_Buffer.size() == 1U) {
			auto elem = _Buffer.front();
			for (auto x = _BeginIdx; x < _EndIdx; x++)
				std::cout << ((elem >> x) & 1U);
		} else {
			auto elem = _Buffer.front();
			for (auto x = _BeginIdx; x < BITS_PER_UINT; x++)
				std::cout << ((elem >> x) & 1U);

			for (auto i = 1U, n = _Buffer.size() - 1U; i < n; i++) {
				elem = _Buffer[i];
				for (auto x = 0U; x < BITS_PER_UINT; x++)
					std::cout << ((elem >> x) & 1U);
			}

			elem = _Buffer.back();
			for (auto x = 0U; x < _EndIdx; x++)
				std::cout << ((elem >> x) & 1U);
		}
	}

	void clear();

private:

	/**
	 *	\brief  ��2^n��Log2
	 */
	template<unsigned int N> struct Log2 {
		static_assert(!(N & (N - 1u)), "�� 2 ���������ݣ�");
		enum { value = Log2<N/2>::value + 1 };
	};
	template<> struct Log2<1> { enum { value = 0 };};

	typedef size_t element;
	static const size_t BITS_PER_UINT = sizeof(element) << 3U;
	static const size_t LOG_BITS_PER_UINT = Log2<sizeof(element)>::value + 3U;

private:

	bool _IsGood;
	size_t _BeginIdx;
	size_t _EndIdx;
	std::deque<element> _Buffer;
};




/*
        000000000000o000
     1010101010101111

	    0000000000o00000
               1111

*/

template<typename T> inline BitStream & BitStream::
operator << (T bits)
{
	static const size_t BIT_PER_T = sizeof(T) << 3U;

	size_t size = BIT_PER_T;		/* ��������ݵ� bit ��С */
	size_t delta = 0U;				/* ��ǰ bits ��ƫ���� */

	/* ����ͷ�� */
	if (size + _EndIdx >= BITS_PER_UINT) {
		_Buffer.back() |= static_cast<element>(bits) << _EndIdx;
		_Buffer.emplace_back();

		delta = BITS_PER_UINT - _EndIdx;
		size -= delta;
		_EndIdx = 0U;
	}

	/* �������� */
	while (size >= BITS_PER_UINT) {
		_Buffer.back() |= static_cast<element>(bits >> delta) << _EndIdx;
		_Buffer.emplace_back();

		size -= BITS_PER_UINT;
		delta += BITS_PER_UINT;
	}

	/* ����ĩβ */
	if (size) {
		_Buffer.back() |= static_cast<element>(bits >> delta) << _EndIdx;
		_EndIdx += size;
	}

	_IsGood = true;
	return *this;
}


/*
		 |		00101111o0000000
		 0000000000000000

		 |		00101111o0000000
		 0000000001011110

0000000000o01011
         0000000001011110

0000000000o01011
         0001011001011110	 
*/

template<typename T> inline BitStream & BitStream::
operator >> (T & bits)
{
	if (eof()) {
		_IsGood = false;
		return *this;
	}

	static const size_t BIT_PER_T = sizeof(T) << 3U;

	size_t size = BIT_PER_T;		/* ��������ݵ� bit ��С */
	size_t delta = 0U;				/* ��ǰ front() ��ƫ���� */

	bits = 0U;

	if (this->size() > size) {
		/* ����ͷ�� */
		if (size + _BeginIdx >= BITS_PER_UINT) {
			bits |= static_cast<T>(_Buffer.front() >> _BeginIdx);
			_Buffer.pop_front();

			delta = BITS_PER_UINT - _BeginIdx;
			size -= delta;
			_BeginIdx = 0U;
		}

		/* �������� */
		while (size >= BITS_PER_UINT) {
			bits |= static_cast<T>(_Buffer.front() >> _BeginIdx) << delta;
			_Buffer.pop_front();

			size -= BITS_PER_UINT;
			delta += BITS_PER_UINT;
		}

		/* ����ĩβ */
		if (size) {
			bits |= static_cast<T>(_Buffer.front() >> _BeginIdx) << delta;
			_BeginIdx += size;
		}

	} else {

		/* ����ͷ�� */
		bits |= static_cast<T>(_Buffer.front() >> _BeginIdx);
		_Buffer.pop_front();
		
		delta = BITS_PER_UINT - _BeginIdx;

		/* �������� */
		while (_Buffer.size() > 1U) {
			bits |= static_cast<T>(_Buffer.front() >> _BeginIdx) << delta;
			_Buffer.pop_front();
			delta += BITS_PER_UINT;
		}

		/* ����ĩβ */
		if (!_Buffer.empty())
			bits |= static_cast<T>(_Buffer.front() >> _BeginIdx) << delta;

		clear();
	}

	_IsGood = true;
	return *this;
}


template<> inline BitStream & BitStream::
operator << (bool is_one)
{
	if (is_one)
		_Buffer.back() |= 1U << _EndIdx;
	else 
		_Buffer.back() &= ~(1U << _EndIdx);

	if (++_EndIdx >= BITS_PER_UINT) {
		_Buffer.emplace_back();
		_EndIdx = 0U;
	}

	if(!_IsGood)
		_IsGood = true;
	
	return *this;
}


template<> inline BitStream & BitStream::
operator >> (bool & bit)
{
	if (eof()) {
		_IsGood = false;
		return *this;
	}
	
	bit = static_cast<bool>((_Buffer.front() >> _BeginIdx) & 1U);
	_Buffer.front() &= ~(static_cast<element>(1U) << _BeginIdx);

	if (++_BeginIdx >= BITS_PER_UINT) {
		_Buffer.pop_front();
		_BeginIdx = 0U;
	}

	if(!_IsGood)
		_IsGood = true;

	return *this;
}
