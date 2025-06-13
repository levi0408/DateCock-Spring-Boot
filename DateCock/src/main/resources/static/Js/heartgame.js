const canvas = document.getElementById("gameCanvas");
const ctx = canvas.getContext("2d");

const playerImg = new Image();
playerImg.src = "/image/캐릭터.png";

const heartImg = new Image();
heartImg.src = "/image/하트.png";

const player = { x: 180, y: 650, width: 60, height: 60, speed: 20 };
const hearts = [];
let score = 0;
let gameOver = false;

document.addEventListener("keydown", (e) => {
  if (e.key === "ArrowLeft" && player.x > 0) player.x -= player.speed;
  if (e.key === "ArrowRight" && player.x + player.width < canvas.width) player.x += player.speed;
});

function drawPlayer() {
  ctx.drawImage(playerImg, player.x, player.y, player.width, player.height);
}

function drawHeart(heart) {
  ctx.drawImage(heartImg, heart.x, heart.y, heart.size, heart.size);
}

function createHeart() {
  const x = Math.random() * (canvas.width - 60);
  hearts.push({ x: x, y: 0, size: 50, speed: 4 + Math.random() * 14 });
}

function checkCollision(heart) {
  return (
    heart.x + heart.size > player.x &&
    heart.x < player.x + player.width &&
    heart.y + heart.size > player.y &&
    heart.y < player.y + player.height
  );
}

function updateGame() {
  if (gameOver) return;

  ctx.clearRect(0, 0, canvas.width, canvas.height);
  drawPlayer();

  for (let i = 0; i < hearts.length; i++) {
    const heart = hearts[i];
    heart.y += heart.speed;
    drawHeart(heart);

    if (checkCollision(heart)) {
      gameOver = true;
      alert("💔 게임 오버! 점수: " + score);
      location.reload();
    }

    if (heart.y > canvas.height) {
      hearts.splice(i, 1);
      score++;
    }
  }

  ctx.fillStyle = "#333";
  ctx.font = "16px Arial";
  ctx.fillText("Score: " + score, 10, 20);

  requestAnimationFrame(updateGame);
}

let loaded = 0;
let heartInterval = null;

[playerImg, heartImg].forEach(img => {
  img.onload = () => {
    loaded++;
    if (loaded === 2) {
      document.getElementById("startButton").disabled = false;
    }
  };
});

document.getElementById("startButton").addEventListener("click", function () {
  if (loaded !== 2) return;

  this.style.display = "none";
  canvas.style.display = "block";
  heartInterval = setInterval(createHeart, 500);
  updateGame();
});
