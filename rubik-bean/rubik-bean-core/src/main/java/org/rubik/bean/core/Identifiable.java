package org.rubik.bean.core;

import java.io.Serializable;

public interface Identifiable<KEY> extends Serializable {

	KEY key();
}
