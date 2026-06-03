pipeline {
    agent any

    environment {
        BLUE_SERVER  = '10.178.0.6'
        GREEN_SERVER = '10.178.0.8'
        APP_PORT     = '8080'
        SSH_USER     = 'chaeyounglim'
        SSH_KEY      = '/var/lib/jenkins/.ssh/id_rsa_samak'
        NGINX_INC    = '/etc/nginx/conf.d/service-url.inc'
        APP_DIR      = '/opt/samak'
        COMPOSE_FILE = 'docker-compose-prod.yml'
    }

    stages {

        // ──────────────────────────────────────────────────────────────
        // 1. 소스 체크아웃 & 커밋 해시 캡처
        // ──────────────────────────────────────────────────────────────
        stage('Checkout') {
            steps {
                checkout scm
                script {
                    env.GIT_COMMIT_SHORT = sh(
                        script: 'git rev-parse --short HEAD',
                        returnStdout: true
                    ).trim()
                    // 롤백용 이전 커밋 해시 (첫 커밋이면 현재 해시로 대체)
                    env.GIT_PREV_COMMIT_SHORT = sh(
                        script: 'git rev-parse --short HEAD~1 2>/dev/null || git rev-parse --short HEAD',
                        returnStdout: true
                    ).trim()
                    echo "배포 커밋: ${env.GIT_COMMIT_SHORT}  /  이전 커밋: ${env.GIT_PREV_COMMIT_SHORT}"
                }
            }
        }

        // ──────────────────────────────────────────────────────────────
        // 2. 테스트 & 빌드
        // ──────────────────────────────────────────────────────────────
        stage('Build') {
            steps {
                sh 'chmod +x ./gradlew'
                sh './gradlew clean bootJar'
            }
        }

        // ──────────────────────────────────────────────────────────────
        // 3. Docker 이미지 빌드 & DockerHub 푸시
        // ──────────────────────────────────────────────────────────────
        stage('Docker Build & Push') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-credentials',
                        usernameVariable: 'DOCKERHUB_USERNAME',
                        passwordVariable: 'DOCKERHUB_TOKEN'
                    ),
                    string(credentialsId: 'dockerhub-repo', variable: 'DOCKERHUB_REPO')
                ]) {
                    script {
                        env.IMAGE_BASE  = "${DOCKERHUB_USERNAME}/${DOCKERHUB_REPO}"
                        env.IMAGE       = "${env.IMAGE_BASE}:${env.GIT_COMMIT_SHORT}"
                        env.PREV_IMAGE  = "${env.IMAGE_BASE}:${env.GIT_PREV_COMMIT_SHORT}"

                        sh """
                            docker build -t '${env.IMAGE}' .
                            echo '${DOCKERHUB_TOKEN}' | docker login -u '${DOCKERHUB_USERNAME}' --password-stdin
                            docker push '${env.IMAGE}'
                            docker tag '${env.IMAGE}' '${env.IMAGE_BASE}:latest'
                            docker push '${env.IMAGE_BASE}:latest'
                        """
                    }
                }
            }
        }

        // ──────────────────────────────────────────────────────────────
        // 4. 현재 Active 서버 파악 → Inactive 서버를 배포 대상으로 설정
        // ──────────────────────────────────────────────────────────────
        stage('Determine Deploy Target') {
            steps {
                script {
                    // service-url.inc 의 IP로 현재 active 판별
                    def active = sh(
                        script: """
                            if grep -q '${BLUE_SERVER}' '${NGINX_INC}' 2>/dev/null; then
                                echo 'blue'
                            else
                                echo 'green'
                            fi
                        """,
                        returnStdout: true
                    ).trim()

                    env.ACTIVE        = active
                    env.INACTIVE      = (active == 'blue') ? 'green' : 'blue'
                    env.DEPLOY_SERVER = (active == 'blue') ? GREEN_SERVER : BLUE_SERVER
                    env.ACTIVE_SERVER = (active == 'blue') ? BLUE_SERVER  : GREEN_SERVER

                    echo "현재 Active  : ${env.ACTIVE} (${env.ACTIVE_SERVER})"
                    echo "배포 대상    : ${env.INACTIVE} (${env.DEPLOY_SERVER})"
                }
            }
        }

        // ──────────────────────────────────────────────────────────────
        // 5. Inactive 서버에 새 이미지 배포
        //    - docker compose pull app 으로 새 이미지만 Pull
        //    - docker compose up -d 로 app 컨테이너만 재시작
        // ──────────────────────────────────────────────────────────────
        stage('Deploy to Inactive Server') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-credentials',
                        usernameVariable: 'DOCKERHUB_USERNAME',
                        passwordVariable: 'DOCKERHUB_TOKEN'
                    )
                ]) {
                    sh """
                        ssh -i ${SSH_KEY} -o StrictHostKeyChecking=no ${SSH_USER}@${env.DEPLOY_SERVER} \
                            "echo '${DOCKERHUB_TOKEN}' | docker login -u '${DOCKERHUB_USERNAME}' --password-stdin \
                             && cd '${APP_DIR}' \
                             && IMAGE_NAME='${env.IMAGE_BASE}' IMAGE_TAG='${env.GIT_COMMIT_SHORT}' docker compose -f '${COMPOSE_FILE}' pull app \
                             && IMAGE_NAME='${env.IMAGE_BASE}' IMAGE_TAG='${env.GIT_COMMIT_SHORT}' docker compose -f '${COMPOSE_FILE}' up -d app \
                             && docker logout"
                    """
                }
            }
        }

        // ──────────────────────────────────────────────────────────────
        // 6. Health Check — 최대 120초(12회 × 10초) 대기
        //    Spring Boot Actuator /actuator/health 엔드포인트 사용
        // ──────────────────────────────────────────────────────────────
        stage('Health Check') {
            steps {
                script {
                    def maxRetries    = 12
                    def retryInterval = 10
                    def healthy       = false

                    for (int i = 1; i <= maxRetries; i++) {
                        def httpCode = sh(
                            script: "curl -sf -o /dev/null -w '%{http_code}' http://${env.DEPLOY_SERVER}:${APP_PORT}/actuator/health || echo '000'",
                            returnStdout: true
                        ).trim()

                        echo "[${i}/${maxRetries}] Health check → HTTP ${httpCode}"

                        if (httpCode == '200') {
                            healthy = true
                            break
                        }
                        if (i < maxRetries) sleep(retryInterval)
                    }

                    if (!healthy) {
                        error('Health check 실패 (120초 초과) — 배포를 롤백합니다')
                    }
                }
            }
        }

        // ──────────────────────────────────────────────────────────────
        // 7. Nginx 트래픽 전환
        // ──────────────────────────────────────────────────────────────
        stage('Switch Traffic') {
            steps {
                sh "${APP_DIR}/scripts/switch-traffic.sh ${env.INACTIVE}"
                script { env.TRAFFIC_SWITCHED = 'true' }
                echo "Nginx 트래픽 → ${env.INACTIVE} (${env.DEPLOY_SERVER})"
            }
        }

        // ──────────────────────────────────────────────────────────────
        // 8. 기존 Active 서버의 app 컨테이너 중단
        // ──────────────────────────────────────────────────────────────
        stage('Stop Old Server') {
            steps {
                sh """
                    ssh -i ${SSH_KEY} -o StrictHostKeyChecking=no ${SSH_USER}@${env.ACTIVE_SERVER} \
                        "cd '${APP_DIR}' \
                         && docker compose -f '${COMPOSE_FILE}' stop app \
                         && docker compose -f '${COMPOSE_FILE}' rm -f app"
                """
            }
        }
    }

    // ──────────────────────────────────────────────────────────────────
    // Post — 성공 / 실패 / 항상
    // ──────────────────────────────────────────────────────────────────
    post {

        // 배포 실패 시 자동 롤백
        failure {
            script {
                echo '파이프라인 실패 — 롤백 시작'

                // 트래픽이 이미 전환된 경우 old active 서버로 복구
                if (env.TRAFFIC_SWITCHED == 'true' && env.ACTIVE?.trim()) {
                    sh "${APP_DIR}/scripts/switch-traffic.sh ${env.ACTIVE} || true"
                    echo "Nginx 트래픽을 ${env.ACTIVE} 서버로 복구 완료"
                }

                // Inactive 서버에 올라간 실패 컨테이너 정리
                if (env.DEPLOY_SERVER?.trim()) {
                    sh """
                        ssh -i ${SSH_KEY} -o StrictHostKeyChecking=no ${SSH_USER}@${env.DEPLOY_SERVER} \
                            "cd '${APP_DIR}' \
                             && docker compose -f '${COMPOSE_FILE}' stop app || true \
                             && docker compose -f '${COMPOSE_FILE}' rm -f app || true \
                             && docker rmi '${env.IMAGE}' || true" || true
                    """
                }

                echo "롤백 완료 — Active 서버(${env.ACTIVE_SERVER ?: '?'})가 계속 서비스 중입니다"
            }
            withCredentials([string(credentialsId: 'discord-webhook-url', variable: 'DISCORD_URL')]) {
                script {
                    def activeServer = env.ACTIVE_SERVER ?: 'N/A'
                    def tag          = env.GIT_COMMIT_SHORT ?: 'N/A'
                    def buildUrl     = env.BUILD_URL ?: ''

                    def payload = groovy.json.JsonOutput.toJson([
                        embeds: [[
                            title : '❌ 배포 실패',
                            color : 15158332,
                            fields: [
                                [name: 'Active 서버', value: activeServer, inline: true],
                                [name: '커밋',        value: tag,          inline: true],
                                [name: '상세 로그',   value: "[콘솔 보기](${buildUrl}console)"]
                            ]
                        ]]
                    ])
                    writeFile file: '/tmp/discord-failure.json', text: payload
                    sh "curl -s -X POST -H 'Content-Type: application/json' -d @/tmp/discord-failure.json \"\$DISCORD_URL\" || true"
                }
            }
        }

        always {
            sh 'docker logout || true'
        }

        success {
            withCredentials([string(credentialsId: 'discord-webhook-url', variable: 'DISCORD_URL')]) {
                script {
                    def server = "${env.INACTIVE ?: 'N/A'} (${env.DEPLOY_SERVER ?: 'N/A'})"
                    def tag    = env.GIT_COMMIT_SHORT ?: 'N/A'
                    sh """curl -s -X POST -H 'Content-Type: application/json' \
                        -d '{"embeds":[{"title":"✅ 배포 성공","color":3066993,"fields":[{"name":"서버","value":"${server}","inline":true},{"name":"커밋","value":"${tag}","inline":true}]}]}' \
                        "\$DISCORD_URL" || true"""
                }
            }
        }
    }
}
