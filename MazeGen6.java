package maze;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Wei Wang
 *
 */
public class MazeGen6 {

    int w, h;
    Cell[][] maze;
    boolean[][] visited;
    int[] dirX = {-1, 1, 0, 0};
    int[] dirY = {0, 0, -1, 1};
    boolean finish;
    boolean found;

    HashMap<Integer, Integer> map;

    JFrame f;					//单元格的高和宽
    JLabel[][] l;
    boolean[][] route;

    MazeGen6(int w, int h) {
        this.w = w;
        this.h = h;
        maze = new Cell[w][h];
        visited = new boolean[w][h];
        map = new HashMap();
        map.put(0, 1);
        map.put(1, 0);
        map.put(2, 3);
        map.put(3, 2);
    }

    public void run() {
        maze[0][0] = new Cell(0, 0);
        maze[0][0].wall[2] = 0;
        visited[0][0] = true;
        dfs(maze[0][0]);
        UI();
        try {
            Thread.sleep(5000);
            UIRoute();
        } catch (InterruptedException ex) {
            Logger.getLogger(MazeGen3.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void dfs(Cell start) {
        if (finish) {
            return;
        }
        if (allVisited()) {
            finish = true;
            return;
        }

        HashSet<Integer> set = new HashSet();
        for (int i = 0; i < 4; i++) {
            int index = (int) Math.floor(Math.random() * 4);
            while (set.contains(index)) {
                index = (int) Math.floor(Math.random() * 4);
            }
            set.add(index);

            int x = start.x + dirX[index], y = start.y + dirY[index];
            if (!inBound(x, y)) {
                continue;
            }
            if (visited[x][y]) {
                continue;
            }
            visited[x][y] = true;
            maze[x][y] = new Cell(x, y);
            start.wall[index] = 0;
            maze[x][y].wall[map.get(index)] = 0;
            dfs(maze[x][y]);

        }

    }

    private boolean inBound(int x, int y) {
        if (x >= 0 && x < w && y >= 0 && y < h) {
            return true;
        }
        return false;
    }

    private boolean allVisited() {
        for (boolean[] tmp : visited) {
            for (boolean now : tmp) {
                if (!now) {
                    return false;
                }
            }
        }
        return true;
    }

    private void UI() {
        JFrame f = new JFrame("Maze");
        f.setSize(1100, 1100);
//        JButton newGame = new JButton("new Game");
//        JButton answer = new JButton("Answer");
//        newGame.setBounds(1000, 300, 100, 50);
//        answer.setBounds(1000, 400, 100, 50);
        Point point = new Point(0, 0);
        f.setLocation(point);
        int grids = w;					        	//行数和列数
        int gridsize = 8;							//单元格的高和宽

        for (int i = 0; i < grids; i++) //外循环控制行
        {
            for (int j = 0; j < grids; j++) //内循环控制列
            {
                JLabel l = new JLabel();			//生成标签实例
                l.setSize(gridsize, gridsize);		//设置标签大小
                l.setLocation(i * gridsize, j * gridsize);	//设置标签位置
                l.setBorder(BorderFactory.createMatteBorder(maze[j][i].wall[0],
                        maze[j][i].wall[2],
                        maze[j][i].wall[1],
                        maze[j][i].wall[3],
                        Color.black));  //设置边界为黑色
                f.add(l);
            }
        }

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

    }

    private void UIRoute() {
        f = new JFrame("Maze");
        l = new JLabel[w][w];
        f.setSize(1100, 1100);
        Point point = new Point(968, 0);
        f.setLocation(point);
        int grids = w;					        	//行数和列数
        int gridsize = 8;

        for (int i = 0; i < grids; i++) //外循环控制行
        {
            for (int j = 0; j < grids; j++) //内循环控制列
            {
                l[i][j] = new JLabel();			//生成标签实例
                l[i][j].setSize(gridsize, gridsize);		//设置标签大小
                l[i][j].setLocation(i * gridsize, j * gridsize);	//设置标签位置

                l[i][j].setBorder(BorderFactory.createMatteBorder(maze[j][i].wall[0],
                        maze[j][i].wall[2],
                        maze[j][i].wall[1],
                        maze[j][i].wall[3],
                        Color.black));  //设置边界为黑色
                f.getContentPane().add(l[i][j]);
            }
        }

        route = new boolean[w][h];
        route[0][0] = true;
        l[0][0].setBackground(Color.red);
        l[0][0].setOpaque(true);
        getRoute();
        f.setBackground(Color.WHITE);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        MazeGen6 gen6 = new MazeGen6(105, 105);
        gen6.run();
    }

    private void getRoute() {
        Stack<int[]> stack = new Stack();
        stack.push(new int[]{0, 0});

        while (!stack.isEmpty()) {
            int[] tmp = stack.peek();
            int x = tmp[0], y = tmp[1];

            if (x == w - 1 && y == h - 1) {
                return;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(MazeGen4.class.getName()).log(Level.SEVERE, null, ex);
            }
            l[y][x].setBackground(Color.red);
            l[y][x].setOpaque(true);
            f.setVisible(true);

            int deadEnd = 0;
            for (int i = 0; i < 4; i++) {
                if (maze[x][y].wall[i] == 1) {
                    deadEnd++;
                    continue;
                }
                int curX = x + dirX[i], curY = y + dirY[i];
                if (!inBound(curX, curY)) {
                    deadEnd++;
                    continue;
                }
                if (route[curX][curY]) {
                    deadEnd++;
                    continue;
                }
                route[curX][curY] = true;
                if (curX == w - 1 && curY == h - 1) {
                    return;
                }

                stack.push(new int[]{curX, curY});

            }
            if (deadEnd == 4) {
                int[] gg = stack.pop();
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MazeGen4.class.getName()).log(Level.SEVERE, null, ex);
                }
                l[gg[1]][gg[0]].setBackground(Color.WHITE);
                l[gg[1]][gg[0]].setOpaque(true);
                f.setVisible(true);
            }
        }
    }
}
