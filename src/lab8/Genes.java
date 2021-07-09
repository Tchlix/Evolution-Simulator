package lab8;

import java.util.Arrays;

public class Genes {
    private final int[] genes_arr = new int[32];

    @Override
    public String toString() {

        return Arrays.toString(genes_arr);
    }
    public Genes()
    {
        int[] count = new int[8];
        for (int i = 0;i < 32;i++)
            count[(int)(Math.random() * (8))] += 1;
        gene_check(count);
        int x = 0;
        for (int i = 0;i < 8;i++)
            for(int j = 0;j < count[i];j++) {
                this.genes_arr[x] = i;
                x += 1;
            }
    }
    public Genes(Genes father,Genes mother)
    {
        int r1 = (int)(Math.random() * (31) + 1);
        int r2 = (int)(Math.random() * (31) + 1);
        while(r1 >= r2)
        {
            r1 = (int)(Math.random() * (31) + 1);
            r2 = (int)(Math.random() * (31) + 1);
        }
        int[] count = new int[8];
         for(int i = 0;i < r1;i++)
            count[father.genes_arr[i]] += 1;
         for (int i = r1;i < r2;i++)
            count[mother.genes_arr[i]] += 1;
         for (int i = r2;i < 32;i++)
            count[father.genes_arr[i]] += 1;
        gene_check(count);
        int x = 0;
        for (int i = 0;i < 8;i++)
            for(int j = 0;j < count[i];j++) {
                this.genes_arr[x] = i;
                x += 1;
            }
    }
    private void gene_check(int[] count)
    {
        boolean flag = true;
        while (flag)
        {
            flag = false;
            int which =- 1;
            for(int i = 0;i < 8;i++) {
                if (count[i] == 0) {
                    flag = true;
                    which = i;
                    break;
                }
            }
            if(which != -1)
            {
                int candidate = (int)(Math.random() * (8));
                while (count[candidate] < 2)
                    candidate = (int)(Math.random() * (8));
                count[candidate] -= 1;
                count[which] += 1;
            }
        }
    }
    public int rotate()
    {
        return genes_arr[(int)(Math.random() * (32))];
    }

    @Override
    public int hashCode() {
        int hash = 0;
        int[] count = new int[8];
        for(int i = 0;i < 32;i++)
        {
            count[genes_arr[i]]++;
        }
        for(int i = 0;i < 8;i++)
        {
            hash += count[i];
            hash *= 10;
        }
        hash /= 10;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Genes)
            return equals((Genes)obj);
        return false;
    }
    public boolean equals(Genes obj)
    {
        for(int i = 0;i < 32;i++)
            if(obj.genes_arr[i] != genes_arr[i])
                return false;
        return true;
    }
}
