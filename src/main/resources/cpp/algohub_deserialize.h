#ifndef ALGOHUB_DESERIALIZE_H
#define ALGOHUB_DESERIALIZE_H

#include "algohub_common.h"
#include <rapidjson/document.h>


template<typename T>
void from_json(const rapidjson::Value &json, T &result);

template<>
void from_json(const rapidjson::Value &json, bool &result) {
    result = json.GetBool();
}

template<>
void from_json(const rapidjson::Value &json, char &result) {
    result = json.GetString()[0];
}

template<>
void from_json(const rapidjson::Value &json, int &result) {
    result = json.GetInt();
}

template<>
void from_json(const rapidjson::Value &json, long long &result) {
    result = json.GetInt64();
}

template<>
void from_json(const rapidjson::Value &json, double &result) {
    result = json.GetDouble();
}

template<>
void from_json(const rapidjson::Value &json, std::string &result) {
    result = std::string(json.GetString());
}

template<typename T>
void from_json(const rapidjson::Value &json, std::vector<T> &vec) {
    assert(json.IsArray());

    for (rapidjson::SizeType i = 0; i < json.Size(); i++) {
        T element;
        from_json(json[i], element);
        vec.push_back(element);
    }
}

template<typename T>
void from_json(const rapidjson::Value &json, std::unordered_set<T> &my_set) {
    assert(json.IsArray());

    for (rapidjson::SizeType i = 0; i < json.Size(); i++) {
        T element;
        from_json(json[i], element);
        my_set.insert(element);
    }
}

// only allow primitive types as keys, this version is for bool keys
template<typename V>
void from_json(const rapidjson::Value &json, std::unordered_map<bool, V> &my_map) {
    assert(json.IsObject());

    for (rapidjson::Value::ConstMemberIterator i = json.MemberBegin(); i != json.MemberEnd(); ++i) {
        // only allow strings as keys
        assert(i->name.IsString());
        const bool key = std::string(i->name.GetString()) == std::string("true");
        V value;
        from_json(i->value, value);
        my_map.insert({ key, value });
    }
}

// only allow primitive types as keys, this version is for char keys
template<typename V>
void from_json(const rapidjson::Value &json, std::unordered_map<char, V> &my_map) {
    assert(json.IsObject());

    for (rapidjson::Value::ConstMemberIterator i = json.MemberBegin(); i != json.MemberEnd(); ++i) {
        // only allow strings as keys
        assert(i->name.IsString());
        const char key = i->name.GetString()[0];
        V value;
        from_json(i->value, value);
        my_map.insert({ key, value });
    }
}

// only allow primitive types as keys, this version is for int keys
template<typename V>
void from_json(const rapidjson::Value &json, std::unordered_map<int, V> &my_map) {
    assert(json.IsObject());

    for (rapidjson::Value::ConstMemberIterator i = json.MemberBegin(); i != json.MemberEnd(); ++i) {
        // only allow strings as keys
        assert(i->name.IsString());
        const int key = std::stoi(std::string(i->name.GetString()));
        V value;
        from_json(i->value, value);
        my_map.insert({ key, value });
    }
}

// only allow primitive types as keys, this version is for long long keys
template<typename V>
void from_json(const rapidjson::Value &json, std::unordered_map<long long, V> &my_map) {
    assert(json.IsObject());

    for (rapidjson::Value::ConstMemberIterator i = json.MemberBegin(); i != json.MemberEnd(); ++i) {
        // only allow strings as keys
        assert(i->name.IsString());
        const long long key = std::stoll(std::string(i->name.GetString()));
        V value;
        from_json(i->value, value);
        my_map.insert({ key, value });
    }
}

// only allow primitive types as keys, this version is for double keys
template<typename V>
void from_json(const rapidjson::Value &json, std::unordered_map<double, V> &my_map) {
    assert(json.IsObject());

    for (rapidjson::Value::ConstMemberIterator i = json.MemberBegin(); i != json.MemberEnd(); ++i) {
        // only allow strings as keys
        assert(i->name.IsString());
        const double key = std::stod(std::string(i->name.GetString()));
        V value;
        from_json(i->value, value);
        my_map.insert({ key, value });
    }
}


// only allow primitive types as keys, this version is for string keys
template<typename V>
void from_json(const rapidjson::Value &json, std::unordered_map<std::string, V> &my_map) {
    assert(json.IsObject());

    for (rapidjson::Value::ConstMemberIterator i = json.MemberBegin(); i != json.MemberEnd(); ++i) {
        // only allow strings as keys
        assert(i->name.IsString());
        std::string key = std::string(i->name.GetString());
        V value;
        from_json(i->value, value);
        my_map.insert({ key, value });
    }
}

template<typename T>
void from_json(const rapidjson::Value &json, std::shared_ptr<LinkedListNode<T>> &list) {
    assert(json.IsArray());

    auto dummy = std::make_shared<LinkedListNode<T>>();
    auto cur(dummy);
    for (rapidjson::SizeType i = 0; i < json.Size(); i++) {
        T element;
        from_json(json[i], element);
        cur->next = std::make_shared<LinkedListNode<T>>(element);
        cur = cur->next;
    }
    list = dummy->next;
}

template<typename T>
void from_json(const rapidjson::Value &json, std::shared_ptr<BinaryTreeNode<T>> &root) {
    assert(json.IsArray());
    if (json.Size() == 0) return;

    //TODO: can not put here, why?
    //    std::queue<std::shared_ptr<BinaryTreeNode<T>>> current, next;
    {
        T first_element;
        from_json(json[0], first_element);
        root = std::make_shared<BinaryTreeNode<T>>(first_element);
    }
    std::queue<std::shared_ptr<BinaryTreeNode<T>>> current, next;

    current.push(root);
    int i = 1;
    while (!current.empty() && i < json.Size()) {
        bool isRightChild = false;
        while (!current.empty() && i < json.Size()) {
            std::shared_ptr<BinaryTreeNode<T>> newNode;
            if (!json[i].IsNull()) {
                T element;
                from_json(json[i], element);
                newNode = std::make_shared<BinaryTreeNode<T>>(element);
            }

            decltype(root) father = current.front();
            if (isRightChild) current.pop();

            if (newNode) {
                next.push(newNode);
                if (isRightChild) {
                    father->right = newNode;
                }
                else {
                    father->left = newNode;
                }
            }
            isRightChild = !isRightChild;
            ++i;
        }
        std::swap(current, next);
    }
}


// for debug
#include <rapidjson/stringbuffer.h>
#include <rapidjson/writer.h>
static void print_json_value(const rapidjson::Value &value) {
    rapidjson::StringBuffer buffer;
    rapidjson::Writer<rapidjson::StringBuffer> writer(buffer);
    value.Accept(writer);

    std::cout << buffer.GetString() << std::endl;
}



#endif //ALGOHUB_DESERIALIZE_H
