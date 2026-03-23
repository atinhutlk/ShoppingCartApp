pipeline {
  agent any

  environment {
    DOCKERHUB_CREDENTIALS_ID = 'Docker-Hub'
    DOCKERHUB_REPO = 'atinhutlk/week7_shoppingcart'
    DOCKER_IMAGE_TAG = 'latest'
  }

  tools {
    maven 'Maven3'
  }

  stages {

    stage('Checkout') {
      steps {
        git branch: 'main',
            url: 'https://github.com/atinhutlk/ShoppingCartApp.git'
      }
    }

    stage('Build & Test + Coverage') {
      steps {
        bat 'mvn -B clean verify'
      }
      post {
        always {
          junit 'target/surefire-reports/*.xml'
        }
      }
    }

    stage('Publish Coverage Report') {
      steps {
        jacoco(execPattern: 'target/jacoco.exec')
      }
    }

    stage('Build Docker Image') {
      steps {
        bat 'docker build -t %DOCKERHUB_REPO%:%DOCKER_IMAGE_TAG% .'
      }
    }

    stage('Push Docker Image') {
      steps {
        withCredentials([
          usernamePassword(
            credentialsId: "${DOCKERHUB_CREDENTIALS_ID}",
            usernameVariable: 'DOCKER_USER',
            passwordVariable: 'DOCKER_PASS'
          )
        ]) {
          bat """
          docker login -u %DOCKER_USER% -p %DOCKER_PASS%
          docker push %DOCKERHUB_REPO%:%DOCKER_IMAGE_TAG%
          """
        }
      }
    }

  }
}