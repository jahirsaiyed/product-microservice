cd productservice & gradlew bootJar -Pprod jibDockerBuild & cd .. & cd reviewservice & gradlew bootJar -Pprod jibDockerBuild & cd .. & cd configserver & gradlew bootJar -Pprod jibDockerBuild & cd .. cd servicesegistry & gradlew bootJar -Pprod jibDockerBuild & cd .. & docker-compose up -d