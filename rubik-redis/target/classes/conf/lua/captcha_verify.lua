local len = #(KEYS)
for index=1, len, 1
do
	if redis.call("get", KEYS[index]) ~= ARGV[index]
	then
		return 1
	end
end
for index=1, len, 1
do
	redis.call("del", KEYS[index])
end
return 0