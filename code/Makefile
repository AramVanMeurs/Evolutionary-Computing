Javafiles=Mutation.java
classfiles=Mutation.class
evalfunc=BentCigarFunction
seed=1

compile:
	javac -cp contest.jar player7.java $(Javafiles)

submission:
	jar cmf MainClass.txt submission.jar player7.class $(classfiles)

run:
	java -jar testrun.jar -submission=player7 -evaluation=$(evalfunc) -seed=$(seed)

comprun:
	make compile && make run
