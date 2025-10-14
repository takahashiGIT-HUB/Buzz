package model;

import java.io.Serializable;
import java.sql.Timestamp;

public record PostInfo (
		int postId,
		String userId,
		String comment,
		byte[] pic,
		String shopName,
		Timestamp postTime,
		String userName)implements Serializable {}