1.Создать 
	ATG для конвертора/парсера  conv.ATG (Calc0_F.ATG)
	Тестовый пример Test0.xxx	(Test0.txt)
	Создать папку для конвертора/парсера  conv (ConvF)
	В этой папке - главную программу (ConvF.java)
	
		package ConvF;
		import java.util.*;
		public class ConvF{
			public static void main(String[] args) {
				String nf = (args.length > 0)? args[0]:"Test0.txt";
				Scanner scan1 = new Scanner(nf); 	
				Parser  pars1 = new Parser(scan1);	
				pars1.Parse(); // запустить анализ
			}	
		} 
		Название пакета и класса == название папки
		По умолчанию исходный файл Test0.xxx 
		Для ConvF, например,  Test0.txt (простой калькулятор)
			var a,b,c
			a = 10
			b = 5*a + 1
			c  = 2 *(a+b)
			print c 
2.Скомпилироать ATG с помощью Coco для получения Scanner.java и Parser.java:
		>coco.bat conv.ATG 
		
3.Перенести Scanner.java и Parser.java в папку conv и добавить в них название пакета

4.Скомпилироать конвертор/парсер
		>jcompConv.bat conv
	В папке образуются
		conv.class		- главная программа
		Buffer.class
		Errors.class
		FatalError.class
		Parser.class
		Scanner.class
		StartStates$Elem.class
		StartStates.class
		Token.class
		UTF8Buffer.class
	Ошибки выводятся в err.txt	

5.Запустить тест (обработка Test0.xxx):
		>Conv_go.bat conv
	Результат выводится на консоль
	При желании можно сохранить:
		>Conv_go.bat conv >res.txt

Для ConvF обработка приведет к программе на языке Forth
	variable a
	variable b
	variable c
	10 a !
	5 a @ * 1 + b !
	2 a @ b @ + * c !
	c @ .
Можно проверить на https://www.tutorialspoint.com/execute_forth_online.php	
		
		
		
	

	
	
