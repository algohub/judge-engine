#ifndef ALGOHUB_JUDGE_RESULT_H
#define ALGOHUB_JUDGE_RESULT_H

#include <signal.h>
#include <iostream>
#include <string>


enum class StatusCode {
    ACCEPTED = 4,
    WRONG_ANSWER = 5,
    RUNTIME_ERROR = 6,
    TIME_LIMIT_EXCEEDED = 7,
    MEMORY_LIMIT_EXCEEDED = 8,
    OUTPUT_LIMIT_EXCEEDED = 9,
    RESTRICTED_CALL = 10,
};

static inline int status_code_to_int(StatusCode code) {
    return static_cast<int>(code);
}

class JudgeResult {
public:
    StatusCode status_code;
    std::string error_message;
    std::string input;
    std::string output;
    std::string expected_output;
    int testcase_passed_count;
    int testcase_total_count;
    long long elapsed_time;
    long long consumed_memory;

    JudgeResult() {
        status_code = StatusCode::ACCEPTED ;
        error_message = "";
        input = "";
        output = "";
        expected_output = "";
        testcase_passed_count = 0;
        testcase_total_count = 0;
        elapsed_time = 0;
        consumed_memory = 0;
    }

    std::string to_json() {
        std::string result("{");
        result += "\"status_code\":" + std::to_string(status_code_to_int(this->status_code)) + ",";
        result += "\"error_message\":" + str_to_json(error_message) + ",";
        result += "\"input\":" + str_to_json(input) + ",";
        result += "\"output\":" + str_to_json(output) + ",";
        result += "\"expected_output\":" + str_to_json(expected_output) + ",";
        result += "\"testcase_passed_count\":" + std::to_string(testcase_passed_count) + ",";
        result += "\"testcase_total_count\":" + std::to_string(testcase_total_count) + ",";
        result += "\"elapsed_time\":" + std::to_string(elapsed_time) + ",";
        result += "\"consumed_memory\":" + std::to_string(consumed_memory);
        result += "}";
        return result;
    }
private:
    std::string str_to_json(const std::string& str) {
        if (str.empty()) {
            return "null";
        } else {
            return "\"" + str + "\"";
        }
    }
};


static JudgeResult judge_result;

static void sigsegv_handler(int sig) {
    judge_result.status_code = StatusCode::RUNTIME_ERROR;
    judge_result.error_message = "Segmentation fault";
    std::cerr << judge_result.to_json() << std::endl;
    exit(sig);
}

static void sigabrt_handler(int sig) {
    judge_result.status_code = StatusCode::RUNTIME_ERROR;
    judge_result.error_message = "Aborted";
    std::cerr << judge_result.to_json() << std::endl;
    exit(sig);
}

#endif //ALGOHUB_JUDGE_RESULT_H
