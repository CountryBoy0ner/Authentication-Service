# ====== СТАДИЯ СБОРКИ ======
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# сначала копируем pom.xml и качаем зависимости (кэшируется)
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

# теперь копируем исходники и собираем jar
COPY src ./src
RUN mvn -q -DskipTests clean package

# ====== СТАДИЯ ЗАПУСКА ======
FROM eclipse-temurin:21-jre
WORKDIR /app

# копируем собранный jar из предыдущей стадии
COPY --from=build /app/target/*.jar app.jar

# порт приложения внутри контейнера
EXPOSE 8082

# переменные окружения (могут прийти сверху, но можно дефолты)
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
