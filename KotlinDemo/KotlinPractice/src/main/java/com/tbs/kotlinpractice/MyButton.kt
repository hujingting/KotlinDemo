package com.tbs.kotlinpractice

import java.util.jar.Attributes
import javax.naming.Context

class MyButton: View {

    constructor(context: Context) : this(context, attributes = Attributes())

    constructor(context: Context, attributes: Attributes) : super(context, attributes)


}