import pygame
import random
import sys

WIDTH, HEIGHT = 600, 400
CELL_SIZE = 20
BASE_FPS = 10

WHITE = (255, 255, 255)
GREEN = (0, 255, 0)
RED   = (255, 0, 0)
BLUE  = (0, 0, 255)
BLACK = (0, 0, 0)
GRAY  = (100, 100, 100)

UP    = (0, -1)
DOWN  = (0, 1)
LEFT  = (-1, 0)
RIGHT = (1, 0)

class SnakeGame:
    def __init__(self, level=1):
        pygame.init()
        self.level = level
        self.screen = pygame.display.set_mode((WIDTH, HEIGHT))
        pygame.display.set_caption("Змейка с уровнями и препятствиями")

        self.clock = pygame.time.Clock()
        self.running = True
        self.game_over = False
        self.paused = False

        self.snake = [(WIDTH // 2, HEIGHT // 2)]
        self.direction = RIGHT
        self.score = 0

        self.speed = BASE_FPS + (level - 1) * 2

        self.food = self.spawn_food()
        self.obstacles = self.generate_obstacles(level)

        self.font = pygame.font.Font(None, 30)

    def spawn_food(self):
        while True:
            fx = random.randint(0, WIDTH // CELL_SIZE - 1) * CELL_SIZE
            fy = random.randint(0, HEIGHT // CELL_SIZE - 1) * CELL_SIZE
            if (fx, fy) not in self.snake and (fx, fy) not in self.obstacles:
                return (fx, fy)

    def generate_obstacles(self, level):
        num_obstacles = level * 3
        obstacles = []
        for _ in range(num_obstacles):
            while True:
                ox = random.randint(0, WIDTH // CELL_SIZE - 1) * CELL_SIZE
                oy = random.randint(0, HEIGHT // CELL_SIZE - 1) * CELL_SIZE
                if (ox, oy) not in self.snake and (ox, oy) != self.food and (ox, oy) not in obstacles:
                    obstacles.append((ox, oy))
                    break
        return obstacles

    def handle_events(self):
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                self.running = False

            elif event.type == pygame.KEYDOWN:
                if event.key == pygame.K_ESCAPE:
                    self.running = False

                if not self.game_over and not self.paused:
                    if len(self.snake) > 1:
                        dx, dy = self.direction
                        if event.key == pygame.K_UP    and (dx, dy) != (0, 1):
                            self.direction = UP
                        elif event.key == pygame.K_DOWN  and (dx, dy) != (0, -1):
                            self.direction = DOWN
                        elif event.key == pygame.K_LEFT  and (dx, dy) != (1, 0):
                            self.direction = LEFT
                        elif event.key == pygame.K_RIGHT and (dx, dy) != (-1, 0):
                            self.direction = RIGHT
                    else:
                        if event.key == pygame.K_UP:
                            self.direction = UP
                        elif event.key == pygame.K_DOWN:
                            self.direction = DOWN
                        elif event.key == pygame.K_LEFT:
                            self.direction = LEFT
                        elif event.key == pygame.K_RIGHT:
                            self.direction = RIGHT

                    if event.key == pygame.K_p:
                        self.paused = not self.paused

                if self.game_over:
                    if event.key == pygame.K_RETURN:
                        self.reset_game()
                    elif event.key == pygame.K_ESCAPE:
                        self.running = False

                if self.paused and not self.game_over:
                    if event.key == pygame.K_p:
                        self.paused = False

    def move_snake(self):
        head_x, head_y = self.snake[0]
        dx, dy = self.direction

        new_head = (head_x + dx * CELL_SIZE, head_y + dy * CELL_SIZE)

        if (new_head in self.snake or
            new_head in self.obstacles or
            new_head[0] < 0 or new_head[0] >= WIDTH or
            new_head[1] < 0 or new_head[1] >= HEIGHT):
            self.game_over = True
            return

        self.snake.insert(0, new_head)

        if new_head == self.food:
            self.score += 10
            self.food = self.spawn_food()
        else:

            self.snake.pop()

    def draw_game(self):

        self.screen.fill(BLACK)


        for segment in self.snake:
            pygame.draw.rect(self.screen, GREEN, (*segment, CELL_SIZE, CELL_SIZE))


        pygame.draw.rect(self.screen, RED, (*self.food, CELL_SIZE, CELL_SIZE))


        for obs in self.obstacles:
            pygame.draw.rect(self.screen, BLUE, (*obs, CELL_SIZE, CELL_SIZE))


        score_text = self.font.render(f"Счет: {self.score}", True, WHITE)
        level_text = self.font.render(f"Уровень: {self.level}", True, WHITE)
        self.screen.blit(score_text, (10, 10))
        self.screen.blit(level_text, (10, 40))

    def draw_game_over(self):

        overlay = pygame.Surface((WIDTH, HEIGHT))
        overlay.set_alpha(180)
        overlay.fill(BLACK)
        self.screen.blit(overlay, (0, 0))

        game_over_text = self.font.render("Игра окончена!", True, WHITE)
        restart_text = self.font.render("Нажмите ENTER, чтобы начать заново", True, GRAY)
        quit_text = self.font.render("Нажмите ESC, чтобы выйти", True, GRAY)


        go_rect = game_over_text.get_rect(center=(WIDTH // 2, HEIGHT // 2 - 30))
        r_rect = restart_text.get_rect(center=(WIDTH // 2, HEIGHT // 2 + 10))
        q_rect = quit_text.get_rect(center=(WIDTH // 2, HEIGHT // 2 + 40))

        self.screen.blit(game_over_text, go_rect)
        self.screen.blit(restart_text, r_rect)
        self.screen.blit(quit_text, q_rect)

    def reset_game(self):

        self.game_over = False
        self.snake = [(WIDTH // 2, HEIGHT // 2)]
        self.direction = RIGHT
        self.score = 0
        self.food = self.spawn_food()
        self.obstacles = self.generate_obstacles(self.level)

    def run(self):

        while self.running:
            self.handle_events()

            if not self.game_over and not self.paused:
                self.move_snake()

            self.draw_game()
            if self.game_over:
                self.draw_game_over()

            pygame.display.flip()
            self.clock.tick(self.speed)

        pygame.quit()
        sys.exit()

def main():

    while True:
        try:
            level = int(input("Выберите уровень (1-5): "))
            if 1 <= level <= 5:
                break
            else:
                print("Пожалуйста, введите число от 1 до 5.")
        except ValueError:
            print("Некорректный ввод! Введите целое число от 1 до 5.")

    game = SnakeGame(level)
    game.run()

if __name__ == "__main__":
    main()
