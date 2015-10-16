#ifndef ALGOHUB_COMMON_H
#define ALGOHUB_COMMON_H

#include <algorithm>
#include <cstdint>
#include <functional>
#include <limits>
#include <memory>  // for std::shared_ptr
#include <queue>
#include <stack>
#include <string>
#include <unordered_map>
#include <unordered_set>
#include <vector>
#include <iostream>

using namespace std;


template<typename E>
class LinkedListNode {
public:
    E value;
    std::shared_ptr<LinkedListNode<E>> next;

    LinkedListNode<E>() { }   // for dummy node
    LinkedListNode<E>(E x) : value(x) { }

    LinkedListNode<E>(const LinkedListNode<E> &other) : value(other.value), next(other.next) { }

    bool operator==(const LinkedListNode<E> &other) const {
        auto lhs = std::make_shared<const LinkedListNode<E>>(*this);
        auto rhs = std::make_shared<const LinkedListNode<E>>(other);

        while (lhs && rhs) {
            if (lhs->value == rhs->value) {
                lhs = lhs->next;
                rhs = rhs->next;
            }
            else {
                return false;
            }
        }
        return !(lhs || rhs);
    }
};


template<class E>
struct BinaryTreeNode {
    E value;
    std::shared_ptr<BinaryTreeNode<E>> left;
    std::shared_ptr<BinaryTreeNode<E>> right;

    BinaryTreeNode<E>() { }  // for dummy node
    BinaryTreeNode<E>(E x) : value(x) { }

    BinaryTreeNode<E>(const BinaryTreeNode<E> &other) : value(other.value),
                                                        left(other.left),
                                                        right(other.right) { }

    bool operator==(const BinaryTreeNode<E> &other) const {
        const auto p = std::make_shared<const BinaryTreeNode<E>>(*this);
        const auto q = std::make_shared<const BinaryTreeNode<E>>(other);
        return isSameTree(p, q);
    }

private:
    static bool isSameTree(const std::shared_ptr<const BinaryTreeNode<E>> &p,
                           const std::shared_ptr<const BinaryTreeNode<E>> &q) {
        if (!p && !q) return true;
        if (!p || !q) return false;
        return p->value == q->value
               && isSameTree(p->left, q->left)
               && isSameTree(p->right, q->right);
    }
};


// template overload, not specialization
template<typename T>
inline bool
operator==(const std::shared_ptr<T> &a, const std::shared_ptr<T> &b) noexcept {
    // use the real operator==() of type T
    return *a == *b;
}


// std::hash
namespace
{
    template<typename T>
    std::size_t make_hash(const T& v) noexcept
    {
        return std::hash<T>()(v);
    }

    void hash_combine(std::size_t& h, const std::size_t& v) noexcept
    {
        h ^= v + 0x9e3779b9 + (h << 6) + (h >> 2);
    }

    template<typename T>
    struct hash_container
    {
        size_t operator()(const T& v) const noexcept
        {
            size_t h = 0;
            for (const auto& e : v) {
                hash_combine(h, make_hash(e));
            }
            return h;
        }
    };
}


namespace std
{
    template<typename T, typename U>
    struct hash<pair<T, U>>
    {
        size_t operator()(const pair<T, U>& v) const noexcept
        {
            size_t h = make_hash(v.first);
            hash_combine(h, make_hash(v.second));
            return h;
        }
    };

    template<typename... T>
    struct hash<vector<T...>> : hash_container<vector<T...>> {};

    template<typename... T>
    struct hash<queue<T...>> : hash_container<queue<T...>> {};

    template<typename... T>
    struct hash<unordered_set<T...>> : hash_container<unordered_set<T...>> {};

    //template<typename K, typename T>
    //struct hash<unordered_map<K, T>> : hash_container<unordered_map<K, T>> {};

    template<typename K, typename T, typename C, typename A>
    struct hash<unordered_map<K, T, C, A>> : hash_container<unordered_map<K, T, C, A>> {};


    // calculate hash on real value
    template<typename T>
    struct hash<shared_ptr<LinkedListNode<T>>> {
        size_t operator()(const shared_ptr<const LinkedListNode<T>> &p) const noexcept {
            return make_hash(*p);
        }
    };

    template<typename T>
    struct hash<shared_ptr<BinaryTreeNode<T>>> {
        size_t operator()(const shared_ptr<const BinaryTreeNode<T>> &p) const noexcept {
            return make_hash(*p);
        }
    };

    // specialized for LinkedListNode
    template<typename T>
    struct hash<LinkedListNode<T>> {
        size_t operator()(const LinkedListNode<T> &list) const noexcept {
            size_t h = make_hash(list.value);
            for (shared_ptr<const LinkedListNode<T>> i = list.next; i; i = i->next) {
                hash_combine(h, make_hash(i->value));
            }
            return h;
        }
    };

    // specialized for BinaryTreeNode
    template<typename T>
    struct hash<BinaryTreeNode<T>> {
        size_t operator()(const BinaryTreeNode<T> &root) const noexcept {
            size_t h = 0;
            std::stack<shared_ptr<const BinaryTreeNode<T>>> my_stack;
            my_stack.push(std::make_shared<const BinaryTreeNode<T>>(root));

            while (!my_stack.empty()) {
                auto node = my_stack.top();
                my_stack.pop();
                hash_combine(h, make_hash(node->value));

                if (node->right != nullptr) my_stack.push(node->right);
                if (node->left != nullptr) my_stack.push(node->left);
            }
            return h;
        }
    };
}


#endif //ALGOHUB_COMMON_H
