package org.rubik.mail;

import java.io.Serializable;
import java.util.Properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailConfig implements Serializable {

	private static final long serialVersionUID = -5476249160388227953L;

	private int port;
	private String host;
	private String from;
	private String protocol = "smtp";
	private String username;
	private String password;
	private String encoding = "UTF-8";
	private Properties properties;
}
