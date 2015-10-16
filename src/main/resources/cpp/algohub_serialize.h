#ifndef ALGOHUB_SERIALIZE_H
#define ALGOHUB_SERIALIZE_H

#include "algohub_common.h"

inline std::string to_json(bool value) {
    return std::to_string(value);
}

inline std::string to_json(int value) {
    return std::to_string(value);
}


inline std::string to_json(long long value) {
    return std::to_string(value);
}


inline std::string to_json(double value) {
    return std::to_string(value);
}


inline std::string to_json(const std::string& myStr) {
    std::string result("\"");
    for (const char ch : myStr) {
        if (ch == '\"') {
            result += "\\" + ch;
        }
        else {
            result += ch;
        }
    }
    result += "\"";
    return result;
}


template <typename T>
std::string to_json(const std::vector<T>& vec) {
    std::string json("[");

    for (auto &i : vec) {
        json += to_json(i);
        json += ",";
    }

    if (!vec.empty()) json.pop_back();
    json += "]";
    return json;
}


template <typename T>
std::string to_json(const std::unordered_set<T>& mySet) {
    std::string json("[");

    for (const auto& i : mySet) {
        json += to_json(i);
        json += ",";
    }

    if (!mySet.empty()) json.pop_back();
    json += "]";
    return json;
}

// only allow primitive types as keys, this version is for string keys
template <typename V>
std::string to_json(const std::unordered_map<std::string, V>& myMap) {
    std::string json("{");

    for (const auto& i : myMap) {
        json += to_json(i.first);
        json += ":";
        json += to_json(i.second);
        json += ",";
    }

    if (!myMap.empty()) json.pop_back();
    json += "}";
    return json;
}

// only allow primitive types as keys, this version is for other
// primitve types except string
template <typename K, typename V>
std::string to_json(const std::unordered_map<K, V>& myMap) {
    std::string json("{");

    for (const auto& i : myMap) {
        json += "\"" + to_json(i.first) + "\"";
        json += ":";
        json += to_json(i.second);
        json += ",";
    }

    if (!myMap.empty()) json.pop_back();
    json += "}";
    return json;
}


template <typename T>
std::string to_json(const std::shared_ptr<const LinkedListNode<T>> &head) {
    std::string json("[");

    for (std::shared_ptr<const LinkedListNode<T>> cur = head; cur; cur = cur->next) {
        json += to_json(cur->value);
        json += ",";
    }
    if (head) json.pop_back();
    json += "]";
    return json;
}

template <typename T>
std::string to_json(const std::shared_ptr<LinkedListNode<T>> &head) {
    const std::shared_ptr<const LinkedListNode<T>> &head_const = head;
    return to_json(head_const);
}


template <typename T>
static std::string to_json(const std::shared_ptr<const BinaryTreeNode<T>>& root) {
    std::string json("[");

    std::queue<std::shared_ptr<const BinaryTreeNode<T>>> current, next;

    if (root) current.push(root);
    while (!current.empty()) {
        std::vector<std::shared_ptr<const BinaryTreeNode<T>>> level;
        while (!current.empty()) {
            auto node = current.front();
            current.pop();
            level.push_back(node);
        }

        int lastNotNullIndex = -1;
        for (int i = level.size() - 1; i >= 0; i--) {
            if (level[i] != nullptr) {
                lastNotNullIndex = i;
                break;
            }
        }

        for (int i = 0; i <= lastNotNullIndex; ++i) {
            auto node = level[i];
            if (node) {
                const std::string& element = to_json(node->value);
                json += element;
            }
            else {
                json += "null";
            }
            json += ",";
            if (node != nullptr) {
                next.push(node->left);
                next.push(node->right);
            }
        }
        std::swap(next, current);
    }

    if (root != nullptr) json.pop_back();
    json += "]";
    return json;
}

template <typename T>
static std::string to_json(const std::shared_ptr<BinaryTreeNode<T>>& root) {
    const std::shared_ptr<const BinaryTreeNode<T>> &root_const = root;
    return to_json(root_const);
}


#endif //ALGOHUB_SERIALIZE_H
