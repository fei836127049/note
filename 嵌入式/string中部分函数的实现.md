在平时的代码中，string.h头文件用到的很多，因此就花了点时间学习一下string库。以下是string库中一些库函数：

```c
/*在参数 *str 所指向的字符串的前 n 个字节中搜索第一次出现字符 c（一个无符号字符）的位置。*/
void memchr(const void *str, int c, size_t n);

/*把 str1 和 str2 的前 n 个字节进行比较。*/
int memcmp(const void *str1, const void *str2, size_t n);

/*从 src 复制 n 个字符到 dest。*/
void *memcpy(void *dest, const void *src, size_t n);

/*另一个用于从 src 复制 n 个字符到 dest 的函数。*/
void *memmove(void *dest, const void *src, size_t n);

/*复制字符 c（一个无符号字符）到参数 str 所指向的字符串的前 n 个字符。*/
void *memset(void *str, int c, size_t n);

/*把 src 所指向的字符串追加到 dest 所指向的字符串的结尾。*/
char *strcat(char *dest, const char *src);

/*把 src 所指向的字符串追加到 dest 所指向的字符串的结尾，直到 n 字符长度为止。*/
char *strncat(char *dest, const char *src, size_t n);

/*在参数 str 所指向的字符串中搜索第一次出现字符 c（一个无符号字符）的位置。*/
char *strchr(const char *str, int c);

/*把 str1 所指向的字符串和 str2 所指向的字符串进行比较。*/
int strcmp(const char *str1, const char *str2);

/*把 str1 和 str2 进行比较，最多比较前 n 个字节。*/
int strncmp(const char *str1, const char *str2, size_t n);

/*把 str1 和 str2 进行比较，结果取决于 LC_COLLATE 的位置设置。*/
int strcoll(const char *str1, const char *str2);

/*把 src 所指向的字符串复制到 dest。*/
char *strcpy(char *dest, const char *src);

/*把 src 所指向的字符串复制到 dest，最多复制 n 个字符。*/
char *strncpy(char *dest, const char *src, size_t n);

/*检索字符串 str1 开头连续有几个字符都不含字符串 str2 中的字符。*/
size_t strcspn(const char *str1, const char *str2);

/*计算字符串 str 的长度，直到空结束字符，但不包括空结束字符。*/
size_t strlen(const char *str);

/*检索字符串 str1 中第一个匹配字符串 str2 中字符的字符，不包含空结束字符。也就是说，依次检验字符串 str1 中的字符，当被检验字符在字符串 str2 中也包含时，则停止检验，并返回该字符位置。*/
char *strpbrk(const char *str1, const char *str2);

/*在参数 str 所指向的字符串中搜索最后一次出现字符 c（一个无符号字符）的位置。*/
char *strrchr(const char *str, int c);

/*检索字符串 str1 中第一个不在字符串 str2 中出现的字符下标。*/
size_t strspn(const char *str1, const char *str2);

/*在字符串 haystack 中查找第一次出现字符串 needle（不包含空结束字符）的位置。*/
char *strstr(const char *haystack, const char *needle);

```

根据在实际的使用，实现了上述部分函数：

```c
/*把 str1 和 str2 的前 n 个字节进行比较。*/
int memcmp(const void *str1, const void *str2, size_t n)
{
	char *str1_ = (char *)str1;
	char *str2_ = (char *)str2;
	if(0 == n)
	{
		return 0;
	}
	while(n-- && *str1_ == *str2_)
	{
		str1_++;
		str2_++;
	}
	return *str1_ - *str2_;
}

/*从 src 复制 n 个字符到 dest。*/
void *memcpy(void *dest, const void *src, size_t n)
{
	void *dest_ = dest;
	if(NULL == dest || NULL == src || 0 == n)
	{
		return dest;
	}
	while(n-- && *(char *)src)
	{
		*(char *)dest++ = *(char *)src++;
	}
	return dest;
}

/*复制字符 c（一个无符号字符）到参数 str 所指向的字符串的前 n 个字符。*/
void *memset(void *str, int c, size_t n)
{
	char * str_ = (char *)str;
	if(NULL == str || 0 == n)
	{
		return str;
	}
	while(n-- > 0)
	{
		*str_++ = c;
	}
	return str;
}

/*把 src 所指向的字符串追加到 dest 所指向的字符串的结尾。*/
char *strcat(char *dest, const char *src)
{
	char * dest_ = dest;
	while(*dest)
	{
		*dest++;
	}
	while(*src)
	{
		*dest++ = *src++;
	}
	return dest;
}

/*把 src 所指向的字符串追加到 dest 所指向的字符串的结尾，直到 n 字符长度为止。*/
char *strncat(char *dest, const char *src, size_t n)
{
	char * p = dest;
	/*将指针p移动到dest末尾*/
	while('\0' != *p)
	{
		p++;
	}
	for(int i = 0; '\0' != src[i] && i < n; i++)
	{
		p[i] = src[i];
	}
	return dest;
}

/*在参数 str 所指向的字符串中搜索第一次出现字符 c（一个无符号字符）的位置。*/
char *strchr(const char *str, int c)
{
	const char * p = str;
	while(*p != c && '\0' != *p)
	{
		p++;
	}
	return (char *)p;
	
}

/*把 str1 所指向的字符串和 str2 所指向的字符串进行比较。*/
int strcmp(const char *str1, const char *str2)
{
	char * str_1 = (char *)str1;
	char * str_2 = (char *)str2;
	int ans = 0;
	assert(NULL != str1 && NULL != str2);
	while(*str_1 == *str_2)
	{
		ans = *str_1 - *str_2;
		if(0 == ans)
		{
			break;
		}
		str_1++;
		str_2++;
	}
	return ans;
	
}

/*把 str1 和 str2 进行比较，最多比较前 n 个字节。*/
int strncmp(const char *str1, const char *str2, size_t n)
{
	char * str_1 = (char *)str1;
	char * str_2 = (char *)str2;
	int ans = 0;
	assert(NULL != str1 && NULL != str2);
	while((*str_1 == *str_2) && n-- > 0)
	{
		ans = *str_1 - *str_2;
		if(0 != ans)
		{
			break;
		}
		str_1++;
		str_2++;
	}
	return ans;

}

/*把 src 所指向的字符串复制到 dest。*/
char *strcpy(char *dest, const char *src)
{
	char * dest_ = dest;
	char * src_ = (char *)src;
	if(NULL == dest || NULL == src)
	{
		return NULL;
	}
	while((*dest_++ = *src_++) != '\0')
	{
		;
	}
	return dest_;
}

/*把 src 所指向的字符串复制到 dest，最多复制 n 个字符。*/
char *strncpy(char *dest, const char *src, size_t n)
{
	char * dest_ = dest;
	while(n > 0)
	{
		if((*dest_ = *src) != '\0')
		{
			src++;
		}
		dest_++;
		n--;
	}
	return dest;
}

/*计算字符串 str 的长度，直到空结束字符，但不包括空结束字符。*/
size_t strlen(const char *str)
{
	char * str_l = (char *)str;
	int len = 0;
	while(*str_l != '\0')
	{
		len++;
		str_l++;
	}
	return len;
}

```

