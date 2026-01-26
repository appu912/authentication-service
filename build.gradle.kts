plugins {
	`java-library`
	id("org.springframework.boot") version "4.0.2"
	id("com.diffplug.spotless") version "8.2.0"
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web:4.0.2")

	compileOnly("org.projectlombok:lombok:1.18.42")
	annotationProcessor("org.projectlombok:lombok:1.18.42")
}

spotless {
	java {
		target("src/*/java/**/*.java")
		importOrder("java|javax", "", "com.progmatic", "com.progmatic.store", "\\#")
		removeUnusedImports()
		googleJavaFormat()
		trimTrailingWhitespace()
		endWithNewline()
		formatAnnotations()
	}
}

