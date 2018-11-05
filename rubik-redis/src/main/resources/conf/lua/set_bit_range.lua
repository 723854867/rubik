for index=ARGV[2], ARGV[1], -1
do
	redis.call("SETBIT", KEYS[1], index, ARGV[3])	
end
