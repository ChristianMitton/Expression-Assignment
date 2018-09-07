# Expression Assignment

</head>

<body style="">
<center>
<h2 data-unsp-sanitized="clean">Programming Assignment 2</h2>
<h2 data-unsp-sanitized="clean">Expression Evaluation</h2>
<h4 data-unsp-sanitized="clean">
In this assignment you will implement a program to evaluate an arithmetic 
expression USING STACKS.
</h4>

<h3 data-unsp-sanitized="clean">Worth 100 points (10% of course grade)</h3>
<h3 data-unsp-sanitized="clean">Posted Mon, June 18
</h3><h3 data-unsp-sanitized="clean">Due Tue, July 3, 1:15 AM (<font color="red">WARNING!! NO GRACE PERIOD</font>)
</h3><h3 data-unsp-sanitized="clean"><font color="red">There is NO extended deadline!!</font>
</h3></center>
<hr>

<ul>
<li>You will work <b>individually</b> on this assignment. Read the
<a href="http://www.cs.rutgers.edu/academic-integrity/programming-assignments" data-unsp-sanitized="clean">
DCS Academic Integrity Policy for Programming Assignments</a> - you are responsible
for this. In particular, note that <b>"All Violations of the Academic
Integrity Policy will be reported by the instructor to the appropriate Dean".</b>


</li><li><h3 data-unsp-sanitized="clean">IMPORTANT - READ THE FOLLOWING CAREFULLY!!!</h3>

<p><font color="red">Assignments emailed to the instructor or TAs will
be ignored--they will NOT be accepted for grading. <br>
We will only grade submissions in Sakai.</font><br>

</p><p><font color="red">If your program does not compile, you will not get any credit.</font> 

</p><p>Most compilation errors occur for two reasons: 
</p><ol>
<li> You 
are programming outside Eclipse, and you delete the "package" statement at the top of the file. 
If you do this, you are changing the program structure, and it will not compile when we
test it.
</li><li> You make some last minute 
changes, and submit without compiling. 
</li></ol>

<h3 data-unsp-sanitized="clean">To avoid these issues, (a) START EARLY, and
give yourself plenty of time to work through the assignment, and (b) Submit a version well
before the deadline so there is at least something in Sakai for us to grade. And you can
keep submitting later versions - we will 
accept the LATEST version.</h3>
</li></ul>

<hr>
<p>
</p><ul>
<li><a href="https://www.ilab.cs.rutgers.edu/~hc691/112/summer_2018/expresssion_description.html#back" data-unsp-sanitized="clean">Arithmetic Expressions Algorithm</a>
</li><li><a href="https://www.ilab.cs.rutgers.edu/~hc691/112/summer_2018/expresssion_description.html#impl" data-unsp-sanitized="clean">Implementation and Grading</a>
</li><li><a href="https://www.ilab.cs.rutgers.edu/~hc691/112/summer_2018/expresssion_description.html#running" data-unsp-sanitized="clean">Running the evaluator</a>
</li><li><a href="https://www.ilab.cs.rutgers.edu/~hc691/112/summer_2018/expresssion_description.html#submission" data-unsp-sanitized="clean">Submission</a>
</li><li><a href="https://www.ilab.cs.rutgers.edu/~hc691/112/summer_2018/expresssion_description.html#faq" data-unsp-sanitized="clean">FAQ - IMPORTANT!!! READ BEFORE ASKING QUESTIONS!!</a>
</li></ul>

<hr>

<a name="expr" data-unsp-sanitized="clean"></a><h3 data-unsp-sanitized="clean">Arithmetic Expressions Algorithm</h3>

Here are some sample expressions of the kind your program will evaluate:

<pre>   3
   Xyz
   3-4*5
   a-(b+A[B[2]])*d+3
   A[2*(a+b)]
   (varx + vary*varz[(vara+varb[(a+b)*33])])/55
</pre>

The expressions will be restricted to the following components:
<ul>
  <li>Integer constants
  </li><li>Scalar (simple, non-array) variables with integer values
  </li><li>Arrays of integers, indexed with a constant or a subexpression
  </li><li>Addition, subtraction, multiplication, and division operators, i.e. 
<tt>'+','-','*','/'</tt> 
  </li><li>Parenthesized subexpressions
</li></ul>

Note the following:
<ul>
  <li>Subexpressions (including indexes into arrays between '[' and ']') may be
nested to any level
  </li><li>Multiplication and division have higher precedence than addition
  and subtraction
  </li><li>Variable names (either scalars or arrays) will be made up of one or more
letters ONLY (nothing but letters a-z and A-Z), are case sensitive (Xyz is different from xyz) and 
will be unique.
</li><li> Integer constants may have multiple digits
</li><li>There may any number of spaces or tabs between any pair of tokens in the 
expression. Tokens are variable names, constants, parentheses, square brackets, and
operators.
</li></ul>

<p></p><hr>
<a name="impl" data-unsp-sanitized="clean"></a><h3 data-unsp-sanitized="clean">Implementation and Grading</h3>

<p>Download the attached <tt>expression_evaluation_project.zip</tt> file to your
computer. DO NOT unzip it. Instead, follow the instructions on the Eclipse page 
under the section "Importing a Zipped Project into Eclipse" to get the entire
project into your Eclipse workspace.

</p><p>You will see a project called <tt>Expression_Evaluation</tt> with 
the following classes in package <tt>expr_eval</tt>:
</p><ul>
<li><tt>ScalarVariable</tt><br>
This class represents a simple scalar variable with a single value. Your implementation
will create a <tt>ScalarVariable</tt> object for every simple variable in the expression.
You don't have to implement anything in this class, so <b>do not make any changes 
to it.</b> 
<p></p></li><li><tt>ArrayVariable</tt><br>
This class represents an array of integer values. 
Your implementation will create an <tt>ArrayVariable</tt> object for every array
variable in the expression (even if there are multiple occurrences of the same array).
You don't have to implement anything in this class, so <b>do not make any changes 
to it.</b> 
<p></p></li><li><tt>Expression</tt><br>
This class represents the expression as a whole, and 
consists all the following steps of the evaluation process:
<ol>
<p></p><li><b>30 pts</b>: <tt>buildVariable</tt> - This method populates
the two instance fields, <tt>scalars</tt> and <tt>arrays</tt>,
with all scalar variables, and all array variables, respectively,
that appear in the expression. <br>
<font color="red">You will fill in the implementation of this method. Make
sure to read the comments above the method header to get more details.</font>
<p></p></li><li><tt>loadVariableValues</tt> - This method reads values for all scalars and
arrays from a file, into the <tt>ScalarVariable</tt> and <tt>ArrayVariable</tt> objects
stored in the <tt>scalars</tt> and <tt>arrays</tt> array lists. This method is
already implemented, <b>do not make any changes</b>.
</li><li><p><b>70 pts</b>: <tt>evaluate</tt> - This method evaluates the expression.<br>
<font color="red">You will fill in the implementation of this method. </font>
</p></li></ol>
<p>Two other methods, <tt>printScalars</tt> and <tt>printArrays</tt> are
implemented for your convenience, and may be used to verify the 
correctness of the scalars and arrays lists after the <tt>buildVariable</tt>
and <tt>loadVariablelValues</tt> methods.

</p></li><li><p><tt>Evaluator</tt>, the application driver, which calls methods in <tt>Expression</tt>
</p></li></ul>
You are also given the following class in package <tt>structure</tt>:
<ul>
 <li><tt>Stack</tt>, to be used in the evaluation process
</li></ul> 
Lastly, two test files are included, <tt>etest1.txt</tt> and <tt>etest2.txt</tt>,
appearing directly under the project folder.

<p>Do not add any other classes. In particular, <font color="red">do NOT
use your own stack class, ONLY use the one you are given.</font> The reason is,
we will be using this same <tt>Stack</tt> class when we test your solution.

</p><p>&nbsp;</p><p><b>Notes on tokenizing the expression</b>

</p><p>You will need to separate out ("tokenize") the components of the
expression in <tt>buildVariable</tt> and <tt>evaluate</tt>. 
Tokens include operands (variables and constants), operators
(<tt>'+','-','*','/'</tt>), parentheses and square brackets.

</p><p>It may be helpful (but you are not required) to use 
<tt>java.util.StringTokenizer</tt> to tokenize the expression.
See the <tt>loadVariableValues</tt> method
in <tt>Expression</tt> for an
example of using <tt>StringTokenizer</tt> to extract variable names. 
The <tt>delims</tt> field in the <tt>Expression</tt> class may be
used in the tokenizing process.

</p><p>The <a href="https://docs.oracle.com/javase/8/docs/api/java/util/StringTokenizer.html" data-unsp-sanitized="clean">
documentation</a> of the <tt>StringTokenizer</tt> class says this:
</p><ul>
"<tt>StringTokenizer</tt> is a legacy class that is retained for compatibility 
reasons although its use is discouraged in new code. It is recommended that 
anyone seeking this functionality use the split method of 
<tt>String</tt> or the <tt>java.util.regex</tt> package instead."
</ul>

<p>For the purpose of this assignment, you may use <tt>StringTokenizer</tt>
without issue. Alternatively, you may use the <tt>split</tt> method of the
<tt>String</tt> class, or the <tt>Pattern</tt> and <tt>Matcher</tt>
classes in the package <tt>java.util.regex</tt>.

</p><p>Or, you may simply parse the expression by scanning it a character
at a time.

</p><p>&nbsp;</p><p><b>Rules while working on <tt>Expression.java</tt></b>:
</p><ul>
<li>You may NOT add any <tt>import</tt> statements to the file. <br>
Note that
the <tt>java.io.*</tt>, <tt>java.util.*</tt>, and <tt>java.util.regex.*</tt>
import statements at the
top of the file allow for using ANY class in <tt>java.io</tt>, <tt>java.util</tt>,
and <tt>java.util.regex</tt> without additional specification or qualification.
</li><li>You may NOT add any fields to the <tt>Expression</tt> class.
</li><li>You may NOT modify the headers of any of the given methods.
</li><li>You may NOT delete any methods.
</li><li>You MAY add helper methods if needed, as long as you make them <tt>private</tt>. (Including the recursive <tt>evaluate</tt> method discussed below.)
</li></ul>

<p>&nbsp;</p><p><b>Rules and guidelines for implementing <tt>evaluate</tt></b>

</p><ul>
<li>An expression may contain sub-expressions within parentheses - you
<font color="red">CAN use RECURSION to evaluate sub-expressions</font>.
<p></p><ul>
There are a couple of coding options if you want to use recursion:

<p>One option is for this recursive method to accept as parameters two
indexes that mark the start and end of the subexpression in the main expression.
So, for instance, if the main expression is
</p><pre>a-(b+A[B[2]])*d+3

01234567891111111  (these are the positions of the chracters in the expression)
          0123456
</pre>
then, to recursively evaluate the subexpression in parantheses, you may
call the recursive evaluate method like this:
<pre>   double res = evaluate(expr, 3, 11);
</pre>
To start with, you may call the recursive evaluate method
from the public <tt>evaluate</tt> method like this (for the above expression):
<pre>   return evaluate(expr, 0, expr.length()-1);
</pre>
which is the entire expression.

<p>Another option is to have your recursive <tt>evaluate</tt> can accept a string as
a parameter, for the subexpression you want to evaluate. In which case,
for the parenthesized subexpression above, you can call it like this:
</p><pre>  double res = evaluate(expr.substring(3,12));
</pre>
And the initial call from the public <tt>evaluate</tt> method would simply be:
<pre>  return evaluate(expr);
</pre>
<p>You can include other parameters in your recursive evaluate, as necessary.
When testing, we will not directly call your recursive evaluate, only the 
public evaluate method with no parameters.
</p></ul>
<p></p></li><li><font color="red">Recursion CAN also be used to evaluate array subscripts</font>
(within '[ and ']'), since a subscript is an expression. <br>
Use the same process as above
to do the recursive call for the subscript expression.

<p></p></li><li>A stack may be used to store the values of operands as well as the
results from evaluating subexpressions - see next point. 
<p></p></li><li>Since <tt>*</tt> and <tt>/</tt> have precedence over <tt>+</tt> and
<tt>-</tt>, it would help to store operators in another stack. (Think
of how you would evaluate <tt>a+b*c</tt> with operands/intermediate results
on one stack and operators on the other.) 

<p></p></li><li>When you implement the <tt>evaluate</tt> method, you may want to test as
you go, implementing code for and testing simple expressions, then building up
to more complex expressions. The following is an example sequence of 
the kinds of expressions you may want to build with:
<ul>
<li><tt>3</tt>
</li><li><tt>a</tt>
</li><li><tt>3+4</tt>
</li><li><tt>a+b</tt>
</li><li><tt>3+4*5</tt>
</li><li><tt>a+b*c</tt>
</li><li>Then introduce parentheses
</li><li>Then try nested parentheses
</li><li>Then introduce array subscripts, but no parentheses
</li><li>Then try nested subscripts, but no parentheses
</li><li>Then try using parentheses as well as array subscripts
</li><li>Then try mixing arrays within parentheses, parentheses within array
subscripts, etc.
</li></ul>
</li></ul>

<p></p><p>You may assume that all input expressions will be correctly formatted,
so you don't need to do any checking of the input expression for correctness.

</p><p>You may also assume that all input symbol values files will be correctly formatted, 
and every file will be guaranteed to have values for all symbols in the expression 
that is being evaluated. So that when you do the evaluation, after
<tt>loadVariableValues</tt> runs, all required scalar and array
values for evaluation will be correctly present in the <tt>arrays</tt>
and <tt>scalars</tt> lists.

</p><p><b>NOTE</b>:
<font color="red">
</font></p><p><font color="red">When we test your <tt>evaluate</tt> method, we will 
use OUR implementation of the
<tt>buildVariable</tt> method. This is for your benefit, so that in the
event that your <tt>buildVariable</tt> does not work correctly, your
<tt>evaluate</tt> method will not be adversely affected.</font>

</p><p></p><hr>
<a name="running" data-unsp-sanitized="clean"></a><h3 data-unsp-sanitized="clean">Running the evaluator</h3>

<p>You can test your implementation by running the <tt>Evaluator</tt> driver
on various expressions and input symbol values file. 

</p><p>When creating your
own symbol values files for testing, make sure they are directly
under the project folder, alongside <tt>etest1.txt</tt> and <tt>etest2.txt</tt>.

</p><p>Since you are not going to turn in the
<tt>Evaluator.java</tt> file, you may introduce debugging statements in 
it as needed.

</p><h4 data-unsp-sanitized="clean">No variables</h4>

<pre>   Enter the expression, or hit return to quit =&gt; 3
   Enter character values file name, or hit return if no characters =&gt; 
   Value of expression = 3.0

   Enter the expression, or hit return to quit =&gt; 3-4*5
   Enter character values file name, or hit return if no characters =&gt; 
   Value of expression = -17.0

   Enter the expression, or hit return to quit =&gt; 
</pre>
Neither of the expressions above have variables, so just hit return to
skip the symbol loading part.

<h4 data-unsp-sanitized="clean">Variables, values loaded from file</h4>

<pre>  Enter the expression, or hit return to quit =&gt; a
  Enter character values file name, or hit return if no characters =&gt; etest1.txt
  Value of expression = 3.0

  Enter the expression, or hit return to quit =&gt; 
</pre>
Since the expression has a variable, <tt>a</tt>, the evaluator needs to be supplied with
a file that has a value for it. Here's what 
<tt>etest1.txt</tt> looks like:
<pre>  a 3
  b 2
  A 5 (2,3) (4,5)
  B 3 (2,1)
  d 56
</pre>
Each line of the file begins with a variable name.  For scalar variables, the
name is followed by the variable's integer value.  For array
variables, the name is followed by the array's length, which is followed by
a series of (index,integer value) pairs. 

<p><font color="red">Note: The index and integer value
pairs must be written with no spaces around the index or integer value.<br>
So, for instance, <tt>(2, 3)</tt> or <tt>( 2,3)</tt> or <tt>(2 ,3)</tt> are all incorrect.<br>
Make sure you
adhere to this requirement when you create your own input files for testing.</font>

</p><p>If the value at a particular array index is not explicitly listed, it is set to 0 by default.

</p><p>So, in the example above, <tt>A = [0,0,3,0,5]</tt> and <tt>B = [0,0,1]</tt>

</p><p>Note that the symbol values file can have values for any number of symbols,
so that it can be used as input for several expressions that contain one or
more of the symbols in the file.

</p><p>Here are a couple more evaluations of expressions for which the symbol values are 
loaded from <tt>etest1.txt</tt>:

</p><pre>  Enter the expression, or hit return to quit =&gt; (a + A[a*2-b])
  Enter character values file name, or hit return if no characters =&gt; etest1.txt
  Value of expression = 8.0

  Enter the expression, or hit return to quit =&gt; a - (b+A[B[2]])*d + 3
  Enter character values file name, or hit return if no characters =&gt; etest1.txt
  Value of expression = -106.0

  Enter the expression, or hit return to quit =&gt;
</pre>

For a change of pace, here's <tt>etest2.txt</tt>, which has
the following symbols and values:
<pre>  varx 6
  vary 5
  arrayA 10 (3,5) (8,12) (9,1)
</pre>
And here are evaluations using this file:
<pre>  
  Enter the expression, or hit return to quit =&gt; arrayA[arrayA[9]*(arrayA[3]+2)+1]-varx
  Enter character values file name, or hit return if no characters =&gt; etest2.txt
  Value of expression = 6.0

  Enter the expression, or hit return to quit =&gt;
</pre>

<p></p><hr>
<a name="submission" data-unsp-sanitized="clean"></a><h3 data-unsp-sanitized="clean">Submission</h3>

Submit your <b>Expression.java</b> file ONLY.

<p></p><hr>
<a name="faq" data-unsp-sanitized="clean"></a><h3 data-unsp-sanitized="clean">Frequently Asked Questions</h3>

<b>Q: Are array names all uppercase?</b>

<p>A:  No. Arrays could have lower case letters in their names. You can tell if a variable is an array if it is followed by an opening square bracket. See, for example, the last example in the "Expression" section, in which <tt>varb</tt> and <tt>varz</tt> are arrays:

</p><pre>(varx + vary*varz[(vara+varb[(a+b)*33])])/55
</pre>

<p><b>Q: Can we delete spaces from the expression?</b>
</p><p>A: Sure.

</p><p><b>Q: Will the expression contain negative numbers?</b>

</p><p>A: No. The expression will NOT have things like <tt>a*-3</tt> or <tt>x+(-y)</tt>. 
It will ONLY have the BINARY
operators +, -, /, and *. In other words, each of these operators will need two values (operands)
to work on. (The <tt>-</tt> in front of <tt>3</tt> in 
<tt>a*-3</tt> is called a UNARY minus. UNARY operators will NOT
appear in the input expression.)

</p><p>However, it is possible that in the process of evaluating the expression, you come
across negative values, either because they appear in the input file, or because
they are the result of evaluation. For instance, when evaluating 
<tt>a+b</tt>, <tt>a=6</tt> and <tt>b=-9</tt> as input
values, and a result of <tt>-3</tt> is a perfectly legitimate scenario. 

</p><p><b>Q: What if an array index evaluates to a non-integer such as 5/2?</b>

</p><p>A: Truncate it and use the resulting integer as the index.

</p><p><b>Q: Could an array index evaluate to a negative integer?</b>

</p><p>A: No, you will not be given any input expression or values that would result in a negative 
integer value for an array index. In other words, you will not need to account for 
this situation in your code.

</p><p><b>Q: Could an array name be the same as a scalar?</b>

</p><p>A: No. All variable names, for both scalars and arrays, are unique.

</p><p><b>Q: Should the expression "()" be reported as an error?</b>

</p><p>A: You don't have to do any error checking on the legality of the expression in the 
<tt>buildVariable</tt> or <tt>evaluate</tt> methods. When these methods are called, you may assume that the expression is correctly constructed. Which means you will not encounter an expression without at least one constant or variable, and all parens and brackets will be correctly formatted. Which means you won't need to deal with "()". 

</p><p><b>Q: Can I convert the expression to postfix, then evaluate the postfix expression?</b>

</p><p>A: NO!!! You have to work with the given traditional/infix form of the expression.

</p><p><b>Q: Can I create new Expression objects and call <tt>evaluate</tt> on them to
handle recursion?</b>

</p><p>A: NO!!! You must work within a single <tt>Expression</tt> object.
