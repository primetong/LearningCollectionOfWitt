/*********************************************************************
 *	@file		HuffmanCoding.cpp
 *	@brief		���ڹ���������
 *********************************************************************/

#include <queue>
#include <stack>
#include <vector>
#include <algorithm>
#include <functional>

#include "BitStack.hpp"
#include "BitStream.hpp"
#include "ImgFuncLib.hpp"


IMGFUNCLIB_API int imgf::
huffman_encode(const byte src[], uint src_size, byte dst[], uint dst_size) 
{
/* ����ĳ��� */
	static const uint MAX_BYTE_NUM = 1U << (sizeof(byte) << 3U);
	static const uint TREE_BIT_SIZE = MAX_BYTE_NUM * (8U + 1U) + 2U * (MAX_BYTE_NUM - 1U) * 2U;
	static const uint HEADER_BIT_SIZE
		= (32U)										/* �ļ���ʼ��־��ռ�õ�λ */
		+ (32U)										/* ������ռ�õ�λ */
		+ (32U)										/* ���������ݳ���ռ�õ�λ */
		+ (TREE_BIT_SIZE)							/* �����ݿ��λ���� */
		;
	static const uint DATA_MAX_BIT_SIZE
		= (src_size << 3U)							/* ���������ݿ��λ���� */
		;


	/* ���㻺���� ��С BIT ��С */
	if (((HEADER_BIT_SIZE + 7U) >> 3U) > dst_size)
		return -1;
		// throw std::runtime_error("buffer size for 'huffman encode' is not enough.");

	BitStream bs;
	{	/* ��ӡͷ�� */
		bs << 0x3F3F3F3FU << TREE_BIT_SIZE << 0U;
	}

	/* K �������ڵ� */
	struct Node {
		std::vector<Node> children;
		byte value;
		inline bool operator < (Node const & rhs) const { return value < rhs.value;}
	} root;

	{	/* ���� K ����������������� */

		/* ͳ��ÿ�������ֵ�Ƶ�� */
		uint cnt[MAX_BYTE_NUM] = {};
		for (auto i = 0U; i < src_size; i++)
			cnt[src[i]]++;

		/* ��ԭʼ�ڵ���뵽���� */
		typedef std::pair<uint, Node> WeightedNode;
		std::priority_queue<WeightedNode, std::vector<WeightedNode>, std::greater<WeightedNode>> heap;
		for (auto i = 0U; i < MAX_BYTE_NUM; i++)
			heap.push(std::make_pair(cnt[i], Node{ {}, static_cast<byte>(i) }));
	
		/* ��ʼ�ϲ��ڵ� */
		while (heap.size() > 1U) {
			/* ���ڵ� */
			Node parent{ {}, 0U };
			parent.children.reserve(2U);

			uint weight = heap.top().first;
			parent.children.emplace_back(std::move(heap.top().second));
			heap.pop();
			
			weight += heap.top().first;
			parent.children.emplace_back(std::move(heap.top().second));
			heap.pop();

			heap.push(std::make_pair(weight, std::move(parent)));
		}

		root = std::move(heap.top().second);
		heap.pop();
	}

	{	/* ��ӡ���� bs */
		auto tree_size = 0U;
		std::stack<Node const *> stack;

		stack.push(&root);
		while (!stack.empty()) {
			/* ȡջ�� */
			auto ptr = stack.top();
			stack.pop();
			
			/* �ж��ǡ� */
			if (ptr == nullptr) {				/* ����һ�������� */
				bs << false << true;
				tree_size += 2U;
			} else {							/* ����һ��Ԫ�� */
				auto & node = *ptr;
				/* ����Ƿ�ΪҶ�ӽڵ� */
				if (node.children.empty()) {	/* ��Ҷ�ӣ��򿽱���ǰ�ַ����б� */
					bs << true << node.value;
					tree_size += 9U;
				} else {						/* ����Ҷ�ӽڵ㣬�����ӽڵ���뵽ջ�� */
					stack.push(nullptr);		/* ������ */
					auto & childs = node.children;
					/* ����ѹ��ջ������ȡ����ʱ����˳��� */
					for (auto riter = childs.rbegin(); riter != childs.rend(); riter++)
						stack.push(&*riter);	/* �ӽڵ� */
					bs << false << false;		/* ������ */
					tree_size += 2U;
				}
			}
		}

		if (tree_size != TREE_BIT_SIZE)
			return -2;
			// throw std::runtime_error("unexcepted tree size.");
	}

	auto data_bit_size = 0U;
	{	/* ��ӡ���� */

		/* �Լ� ���� [0, 255] �Ķ���������ӳ��� */
		std::vector<BitStack> table(MAX_BYTE_NUM);
		std::stack<std::pair<Node const *, BitStack>> stack;

		stack.push(std::make_pair(&root, BitStack{}));
		while (!stack.empty()) {
			/* ȡջ�� */
			auto & top = *stack.top().first;
			auto path = std::move(stack.top().second);
			stack.pop();

			/* ����Ƿ�ΪҶ�ӽڵ� */
			if (top.children.empty()) {
				/* ��Ҷ�ӣ��򿽱���ǰ���루·�������б� */
				table[top.value] = path;
			} else {
				/* ����Ҷ�ӽڵ㣬�����ӽڵ���뵽ջ�� */
				uint i = 0U;
				for (auto & child : top.children) {
					path.push(i++ != 0U);			/* Լ��������Ϊ 0���Һ���Ϊ 1 */
					stack.push(std::make_pair(&child, path));
					path.pop();
				}
			}
		}

		/* ����ӳ�������������� bs */
		for (auto i = 0U; i < src_size; i++) {
			auto & bitstack = table[src[i]];
			bs << bitstack;
			data_bit_size += bitstack.size();
		}
	}

	/* ͳ���ļ����ȣ��жϻ������Ƿ� */
	auto total_size = (HEADER_BIT_SIZE + data_bit_size + 7U) >> 3U;
	if (total_size > dst_size)
		return -3;
		// throw std::runtime_error("buffer size for 'huffman encode' is not enough.");

	/* ��� */
	for (auto i = 0U; bs >> dst[i]; i++);			/* ����� dst */

	reinterpret_cast<uint *>(dst)[2] = data_bit_size;/* �޸�������ĳ��� */
	return static_cast<int>(total_size);			/* ����ʵ�ʳ��� */
}


IMGFUNCLIB_API int imgf::
huffman_decode(const byte src[], uint src_size, byte dst[], uint dst_size)
{
	/* ����ļ�ͷ */
	if (reinterpret_cast<uint const *>(src)[0] != 0x3F3F3F3FU)
		return -1;

	/* �������ݵ����� */
	BitStream bs;
	for (auto i = sizeof(uint) / sizeof(byte); i < src_size; i++)
		bs << src[i];

	/* ��ȡ�������� */
	uint tree_bit_size = 0U, data_bit_size = 0U;
	bs >> tree_bit_size >> data_bit_size;
	
	/* K �������ڵ� */
	struct Node {
		std::vector<Node> children;
		byte value;
		inline bool operator < (Node const & rhs) const { return value < rhs.value;}
	} root;

	{	/* ��ȡ�������� */
		static const uint TREE_DEEPTH_LIMIT = 257U; // ��ʱ����
		bool bit = false;
		
		std::stack<Node *> stack;
		stack.push(&root);

		auto bit_cnt = 0U;
		while (!stack.empty() && bit_cnt < tree_bit_size) {
			bs >> bit;
			bit_cnt++;

			if (bit) {		/* ��� */
				byte value;
				bs >> value;
				bit_cnt += 8U;
				stack.top()->children.emplace_back(std::move(Node{ {}, value }));
			} else {		/* ���� */
				bool is_right = false;
				bs >> is_right;
				bit_cnt++;

				if (is_right) {	/* ������ */
					stack.pop();
				} else {
					auto & top_children = stack.top()->children;
					top_children.emplace_back();
					stack.push(&top_children.back());
				}
			}
		}

		if (bit_cnt != tree_bit_size)
			return -2;
			// throw std::runtime_error("unexcepted tree size.");
	}

	uint actual_data_size = 0U;
	{	/* �������е����ݣ����� bs ��ʣ�µ� data */
		bool bit = false;
		Node const * cur = &root.children.front();
		for (auto i = 0U; i < data_bit_size && bs; i++) {
			bs >> bit;
			cur = bit						/* Լ��������Ϊ 0���Һ���Ϊ 1 */
				? &cur->children.back()
				: &cur->children.front()
				;
			if (cur->children.empty()) {				/* ����Ҷ�ӽڵ� */
				if (actual_data_size > dst_size)
					return -3;
					// throw std::runtime_error("data size is not enough.");

				dst[actual_data_size++] = cur->value;	/* �������� */
				cur = &root.children.front();			/* ���� */
			}
		}
		
		/* �ж��������Ƿ����� */
		if (cur != &root.children.front())
			return -4;
			// throw std::runtime_error("data is incomplete.");
	}

	return static_cast<int>(actual_data_size);
}
