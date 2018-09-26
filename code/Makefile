Javafiles=Mutation.java Selection.java
classfiles=Mutation.class Selection.class
evalfunc=BentCigarFunction
seed=1

compile:
	javac -cp contest.jar player7.java $(Javafiles)

submission:
	jar cmf MainClass.txt submission.jar player7.class $(classfiles)

run:
	java -jar testrun.jar -submission=player7 -evaluation=$(evalfunc) -seed=$(seed)

full:
	make compile && make submission && make run
