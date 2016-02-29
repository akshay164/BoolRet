# BoolRet
Boolean Information Retrieval Techniques. (Term at a time and Document at a time retrieval)
The program uses a custom data structure which acts as a dictionary.

A Hashmap contains the tokens as keys. The value is an object which has 2 postings lists as linked lists.
1 of the postings list is ordered by DocID. The other is by term frequency.

Search queries are read by a file. With each line having a list of words which are queried with OR and AND operators.
i.e. words are queried conjunctively and disjunctively. 

Term at a Time and Document at a Time algorithms are shown. (DAAT being the efficient one)
