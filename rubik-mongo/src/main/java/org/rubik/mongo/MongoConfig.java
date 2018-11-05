package org.rubik.mongo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MongoConfig implements Serializable {

	private static final long serialVersionUID = 5103745506163482846L;

	private String db;
	private String host;
	private String username;
	private String password;
}
