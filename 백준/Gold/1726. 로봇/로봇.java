import java.io.*;
import java.util.*;


public class Main {
    //다익스트라 탐색할 때 로봇 정보
    static class Info implements Comparable<Info>{
        //r : y, c : x
        //cnt : 명령 횟수, dir : 방향
        int r, c, cnt, dir;
        Info(int r, int c, int cnt, int dir){
            this.r = r;
            this.c = c;
            this.cnt = cnt;
            this.dir = dir;
        }
        //명령 횟수 기준 오름차순 정렬
        @Override
        public int compareTo(Info o){
            return this.cnt - o.cnt;
        }
    }
    public static void main(String[] args) throws IOException {
        //입력값 처리하는 BufferedReader
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        //결과값 출력하는 BufferedWriter
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine()," ");
        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());
        int[][] map = new int[N][M];
        //공장 정보 저장
        for(int i=0;i<N;i++){
            st = new StringTokenizer(br.readLine()," ");
            for(int j=0;j<M;j++){
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        int[][] position = new int[2][3];
        //시작 위치, 도착 위치 정보 저장
        for(int i=0;i<2;i++){
            st = new StringTokenizer(br.readLine()," ");
            position[i][0] = Integer.parseInt(st.nextToken()) - 1;
            position[i][1] = Integer.parseInt(st.nextToken()) - 1;
            //{동, 서, 남, 북} -> { 북, 동, 남, 서 } 형식으로 변경
            position[i][2] = setDir(Integer.parseInt(st.nextToken()));
        }
        //다익스트라 탐색
        int result = bfs(map, position, N, M);
        //최소 명령 횟수 BufferedWriter 저장
        bw.write(String.valueOf(result));
        bw.flush();		//결과 출력
        bw.close();
        br.close();
    }
    //다익스트라를 통해서 최소 명령 횟수 탐색하는 함수
    static int bfs(int[][] map, int[][] position, int N, int M){
        PriorityQueue<Info> pq = new PriorityQueue<>();
        //북동남서 이동하는 변경 r, c의 값
        int[] dr = {-1, 0, 1, 0};
        int[] dc = {0 , 1, 0, -1};
        //방문 확인 배열
        boolean[][][] visited = new boolean[N][M][4];
        //시작 위치를 기준으로 정보 저장
        visited[position[0][0]][position[0][1]][position[0][2]] = true;
        pq.offer(new Info(position[0][0], position[0][1], 0,  position[0][2]));
        //다익스트라 진행
        while(!pq.isEmpty()){
            Info cur = pq.poll();
            //도착 위치 도달 시
            if(cur.r == position[1][0] && cur.c == position[1][1] && cur.dir == position[1][2]){
                return cur.cnt;
            }
            //현재 방향으로 1, 2, 3칸 이동
            int nr = cur.r;
            int nc = cur.c;
            for(int i=1;i<=3;i++){
                nr += dr[cur.dir];
                nc += dc[cur.dir];
                //공장에 벗어나거나, 경로가 막힐 때
                if(!inSpace(nr, nc, N, M) || map[nr][nc] == 1){
                    break;
                }
                //이미 방문한 공간일 때
                if(visited[nr][nc][cur.dir]){
                    continue;
                }
                //전진!
                visited[nr][nc][cur.dir] = true;
                pq.offer(new Info(nr, nc, cur.cnt + 1, cur.dir));
            }

            //오른쪽 회전
            int rd = (cur.dir + 1) % 4;
            if(!visited[cur.r][cur.c][rd]){
                visited[cur.r][cur.c][rd] = true;
                pq.offer(new Info(cur.r, cur.c, cur.cnt + 1, rd));
            }
            //왼쪽 회전
            int ld = (cur.dir - 1) < 0 ? 3 : cur.dir - 1;
            if(!visited[cur.r][cur.c][ld]){
                visited[cur.r][cur.c][ld] = true;
                pq.offer(new Info(cur.r, cur.c, cur.cnt + 1, ld));
            }
        }
        return 0;
    }
    //{동, 서, 남, 북} -> {북, 동, 남, 서} 변경 함수
    static int setDir(int dir){
        if(dir == 1){	//동 -> 동
            return 1;
        }else if(dir == 2){ //서 -> 동
            return 3;
        }else if(dir == 3){ // 남 -> 남
            return 2;
        }else{ //북 -> 서
            return 0;
        }
    }
    //이동하려는 칸이 공장에 존재하는지 확인하는 함수
    static boolean inSpace(int r, int c, int N, int M){
        if(r >= 0 && c >= 0 && r < N && c < M){
            return true;
        }
        return false;
    }
}

