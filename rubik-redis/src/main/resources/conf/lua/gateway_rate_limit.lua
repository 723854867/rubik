local len = #(ARGV)
if (len == 1) 
then
	local exists = redis.call("exists", KEYS[1])
	if (exists == 1)
	then
		return 2
	end
	redis.call("set", KEYS[1], 1, "PX", ARGV[1])
	return 0
else
	local keyType = redis.call("type", KEYS[1])["ok"]
	if (keyType == "none" or keyType == "zset") 
	then
		local val = redis.call("pttl", KEYS[1])
		if ((val ~= -1) and (val ~= -2))
		then
			redis.call("zremrangebyscore", KEYS[1], 0, ARGV[3])
			local count = redis.call("zcount", KEYS[1], "-inf", "+inf")
			if (count >= tonumber(ARGV[4]))
			then
				return 1
			end
			local recoveryTime = tonumber(ARGV[5])
			if (recoveryTime > 0)
			then
				local member = redis.call("zrange", KEYS[1], -1, -1)
				local lastVistorTimestamp = member[1]
				local delt = ARGV[1] - lastVistorTimestamp
				if (delt < recoveryTime)
				then
					return 2
				end
			end
		end
		redis.call("zadd", KEYS[1], ARGV[1], ARGV[1])
		redis.call("pexpire", KEYS[1], ARGV[2])
		return 0
	end
	return 2
end 