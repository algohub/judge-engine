import json


class JudgeResult(object):
    def __init__(self, status_code, error_message=None, input=None,
                 output=None, expected_output=None, testcase_passed_count=0,
                 testcase_total_count=0, elapsed_time=0, consumed_memory=0):
        self.status_code = status_code
        self.error_message = error_message
        self.input = input
        self.output = output
        self.expected_output = expected_output
        self.testcase_passed_count = testcase_passed_count
        self.testcase_total_count = testcase_total_count
        self.elapsed_time = elapsed_time
        self.consumed_memory = consumed_memory

    def to_json(self):
        result = '{'
        result += '"status_code":' + str(self.status_code.value)
        if self.error_message is not None:
            result += '， "error_message":' + json.dumps(self.error_message)
        if self.input is not None:
            result += '， "input":' + json.dumps(self.input)
        if self.output is not None:
            result += '， "output":' + json.dumps(self.output)
        if self.expected_output is not None:
            result += '， "expected_output":' + json.dumps(self.expected_output)
        if self.testcase_passed_count is not None and self.testcase_passed_count != 0:
            result += '， "testcase_passed_count":' + str(self.testcase_passed_count)
        if self.testcase_total_count is not None and self.testcase_total_count != 0:
            result += '， "testcase_total_count":' + str(self.testcase_total_count)
        if self.elapsed_time is not None and self.elapsed_time != 0:
            result += '， "elapsed_time":' + str(self.elapsed_time)
        if self.consumed_memory is not None and self.consumed_memory != 0:
            result += '， "consumed_memory":' + str(self.consumed_memory)
        result += '}'
        return result
