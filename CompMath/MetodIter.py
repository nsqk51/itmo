import sys
from dataclasses import dataclass, field


class InputError(Exception):
    pass


@dataclass
class IterationMethod:
    accuracy: float = 0.1
    size: int = 0
    matrix: list[list[float]] = field(default_factory=list)
    right_parts: list[float] = field(default_factory=list)
    MAX_ITERATION_COUNT: int = 1000

    perm: list[int] = field(default_factory=list)
    

    def read_interactive(self) -> None:
        
        while True:
            mode = input("Источник данных: [1] клавиатура, [2] файл: ").strip()
            if mode in ("1", "2"):
                break
            print("Ошибка: введите 1 или 2.")

        if mode == "1":
            self._read_from_keyboard()
        else:
            self._read_from_file_prompt()

        self._validate_dimensions()
        self._split_augmented_matrix()
        self.perm = list(range(self.size))

    def _read_from_keyboard(self) -> None:
        self.accuracy = self._read_float("Введите точность (например 0.001): ")
        self.size = self._read_int("Введите размерность n: ")

        print("Введите расширенную матрицу A|b построчно.")
        self.matrix = []
        for i in range(self.size):
            row = self._read_float_row(f"")
            self.matrix.append(row)

    def _read_from_file_prompt(self) -> None:
        path = input("Введите путь к файлу: ").strip()
        self.accuracy = self._read_float("Введите точность (например 0.001): ")

        self.matrix = []
        with open(path, "r", encoding="utf-8") as f:
            for raw_line in f:
                line = raw_line.strip()
                if not line:
                    continue
                parts = line.replace(",", " ").split()
                try:
                    row = list(map(float, parts))
                except ValueError as e:
                    raise InputError(f"Не удалось распарсить строку файла: {raw_line!r}") from e
                self.matrix.append(row)

        self.size = len(self.matrix)
        if self.size == 0:
            raise InputError("Файл пустой: нет ни одной строки с матрицей.")

    def _validate_dimensions(self) -> None:
        if self.size <= 0:
            raise InputError("Некорректный размер матрицы.")

        if len(self.matrix) != self.size:
            raise InputError(f"Ожидалось {self.size} строк, получено {len(self.matrix)}.")

        expected_cols = self.size + 1
        for i, row in enumerate(self.matrix):
            if len(row) != expected_cols:
                raise InputError(
                    f"Некорректная длина строки {i+1}: ожидалось {expected_cols} чисел (n+1), "
                    f"получено {len(row)}. Строка: {row}"
                )

    def _split_augmented_matrix(self) -> None:
        self.right_parts = []
        for i in range(self.size):
            self.right_parts.append(self.matrix[i].pop())

    @staticmethod
    def _read_int(prompt: str) -> int:
        while True:
            s = input(prompt).strip()
            try:
                val = int(s)
                if val <= 0:
                    print("Ошибка: число должно быть > 0.")
                    continue
                return val
            except ValueError:
                print("Ошибка: введите целое число.")

    @staticmethod
    def _read_float(prompt: str) -> float:
        while True:
            s = input(prompt).strip().replace(",", ".")
            try:
                val = float(s)
                if val <= 0:
                    print("Ошибка: число должно быть > 0.")
                    continue
                return val
            except ValueError:
                print("Ошибка: введите число (например 0.001).")

    @staticmethod
    def _read_float_row(prompt: str) -> list[float]:
        while True:
            s = input(prompt).strip()
            if not s:
                print("Ошибка: строка пустая.")
                continue
            parts = s.replace(",", " ").split()
            try:
                return list(map(float, parts))
            except ValueError:
                print("Ошибка: не удалось преобразовать строку в числа. Пример: 1 2 3 4")


    def dominance_report(self) -> tuple[bool, bool, list[tuple[int, float, float]]]:

        strictly = False
        details: list[tuple[int, float, float]] = []

        for i in range(self.size):
            diag = abs(self.matrix[i][i])
            sum_others = sum(abs(x) for j, x in enumerate(self.matrix[i]) if j != i)
            details.append((i, diag, sum_others))

            if diag < sum_others:
                return False, False, details
            if diag > sum_others:
                strictly = True

        return True, strictly, details

    def make_diagonally_dominant(self) -> bool:
        ok, _, _ = self.dominance_report()
        if ok:
            return True

        n = self.size
        used_rows = [False] * n
        used_cols = [False] * n
        row_choice = [-1] * n
        col_choice = [-1] * n

        abs_row_sums = [sum(abs(v) for v in self.matrix[r]) for r in range(n)]

        def can_place(i: int, r: int, c: int) -> bool:
            diag = abs(self.matrix[r][c])
            sum_others = abs_row_sums[r] - abs(self.matrix[r][c])
            return diag >= sum_others

        def dfs(i: int) -> bool:
            if i == n:
                return True
            for r in range(n):
                if used_rows[r]:
                    continue
                for c in range(n):
                    if used_cols[c]:
                        continue
                    if can_place(i, r, c):
                        used_rows[r] = True
                        used_cols[c] = True
                        row_choice[i] = r
                        col_choice[i] = c
                        if dfs(i + 1):
                            return True
                        used_rows[r] = False
                        used_cols[c] = False
                        row_choice[i] = -1
                        col_choice[i] = -1
            return False

        if not dfs(0):
            return False

        newA = [[0.0] * n for _ in range(n)]
        newb = [0.0] * n
        for i in range(n):
            r = row_choice[i]
            newb[i] = self.right_parts[r]
            for j in range(n):
                newA[i][j] = self.matrix[r][col_choice[j]]

        self.matrix = newA
        self.right_parts = newb

        self.perm = [self.perm[col_choice[j]] for j in range(n)]

        ok2, _, _ = self.dominance_report()
        return ok2


    def matrix_norm_inf(self) -> float:

        return max(sum(abs(x) for x in row) for row in self.matrix) if self.matrix else 0.0


    def solve(self) -> tuple[list[float], int, list[float]]:

        for i in range(self.size):
            if abs(self.matrix[i][i]) < 1e-15:
                raise ZeroDivisionError(
                    f"Нулевой/почти нулевой диагональный элемент a[{i}][{i}]={self.matrix[i][i]}."
                )

        n = self.size
        C = [
            [(-self.matrix[i][j] / self.matrix[i][i]) if i != j else 0.0 for j in range(n)]
            for i in range(n)
        ]
        D = [self.right_parts[i] / self.matrix[i][i] for i in range(n)]

        X = D.copy()
        iter_count = 0

        while True:
            iter_count += 1
            if iter_count > self.MAX_ITERATION_COUNT:
                raise RuntimeError("Превышено максимальное количество итераций. Метод не сошёлся.")

            X_next = [sum(C[i][j] * X[j] for j in range(n)) + D[i] for i in range(n)]
            diff = [abs(X_next[i] - X[i]) for i in range(n)]

            if max(diff) < self.accuracy:
                
                X_original = [0.0] * n
                for new_idx in range(n):
                    orig_idx = self.perm[new_idx]
                    X_original[orig_idx] = X_next[new_idx]
                return X_original, iter_count, diff

            X = X_next


    #{:10.6g} — формат: ширина 10 символов, 6 значащих цифр
    def print_system(self) -> None:
        for i, row in enumerate(self.matrix):
            for element in row:
                print("{:10.6g}".format(element), end=" ")
            print("| {:10.6g}".format(self.right_parts[i]))

    @staticmethod
    def print_vector_x(X: list[float]) -> None:
        for i, val in enumerate(X, start=1):
            print(f"x{i} = {val}")


def main():
    solver = IterationMethod()
    try:
        solver.read_interactive()

        print("\nИсходная система (A|b):")
        solver.print_system()

        print("\nНорма матрицы:")
        print(solver.matrix_norm_inf())

        ok, strictly, details = solver.dominance_report()
        if ok:
            print("Диагональное преобладание уже выполнено.")
        else:
            print("Диагонального преобладания нет. Выполняю перестановки строк/столбцов...")
            if solver.make_diagonally_dominant():
                print("Успех: диагональное преобладание достигнуто перестановками.")
            else:
                print(
                    "Невозможно достичь диагонального преобладания перестановками строк/столбцов.",
                    file=sys.stderr,
                )

        print("\nСистема после перестановок:")
        solver.print_system()

        X, iter_count, errors = solver.solve()

        print("\nВектор неизвестных:")
        solver.print_vector_x(X)

        print("\nКоличество итераций, за которое найдено решение:")
        print(iter_count)

        print("\nВектор погрешностей последнего шага:")
        print(errors)

    except (InputError, FileNotFoundError) as e:
        print(f"Ошибка ввода: {e}", file=sys.stderr)
    except Exception as e:
        print(f"Ошибка: {e}", file=sys.stderr)


if __name__ == "__main__":
    main()