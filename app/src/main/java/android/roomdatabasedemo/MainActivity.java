package android.roomdatabasedemo;

import android.app.Activity;
import android.os.Bundle;
import android.roomdatabasedemo.database.SampleDatabase;
import android.roomdatabasedemo.database.entity.NameEntity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Activity activity;
    RecyclerView recyclerView;
    Button btnInsert;
    EditText etName;
    SampleDatabase sampleDatabase;
    ArrayList<NameEntity> nameEntities;
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        initializationDatabase();
        mappingWidgets();
        fetchAllData();
    }
    private void fetchAllData() {
        nameEntities = (ArrayList<NameEntity>) sampleDatabase.daoAccess().fetchAllRecords();
        if (nameEntities != null) {
            myAdapter = new MyAdapter(nameEntities);
            recyclerView.setAdapter(myAdapter);
        }
    }

    /**
     * max 10 record display
     */
    private void initializationDatabase() {
        sampleDatabase = SampleDatabase.getSampleDatabase(activity);
        String strSql = "CREATE TRIGGER IF NOT EXISTS delete_till_10 INSERT ON user WHEN (select count(*) from user)>9 BEGIN DELETE FROM user WHERE id IN  (SELECT id FROM user ORDER BY id limit (select count(*) -9 from user)); END;";
        sampleDatabase.getOpenHelper().getWritableDatabase().execSQL(strSql);
    }

    public void mappingWidgets() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        btnInsert = (Button) findViewById(R.id.btnInsert);
        etName = (EditText) findViewById(R.id.etName);

        recyclerView.setLayoutManager(new LinearLayoutManager(activity));


        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etName.getText().toString().length() > 0) {
                    final String name = etName.getText().toString();

                    NameEntity nameEntity = new NameEntity();
                    nameEntity.setName(name);

                    final long id = sampleDatabase.daoAccess().insertRecord(nameEntity);

                    Toast.makeText(activity, "record inserted :: " + id, Toast.LENGTH_SHORT).show();
                    fetchAllData();
                }
            }
        });
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        ArrayList<NameEntity> names = new ArrayList<NameEntity>();

        public MyAdapter(ArrayList<NameEntity> nameEntities) {
            this.names = nameEntities;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_view, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.tvName.setText(names.get(position).getId() + "  " + names.get(position).getName());
            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = sampleDatabase.daoAccess().deleteRecord(names.get(position));
                    Toast.makeText(activity, "record deleted :: " + id, Toast.LENGTH_SHORT).show();
                    fetchAllData();
                }
            });
        }

        @Override
        public int getItemCount() {
            return names.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvName;
            ImageView ivDelete;

            public ViewHolder(View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvName);
                ivDelete =  itemView.findViewById(R.id.ivDelete);
            }
        }
    }

}
