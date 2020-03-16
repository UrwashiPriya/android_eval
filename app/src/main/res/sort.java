  public void onClick(View v) {

          if (v == findViewById(R.id.button2)) {

                 final CharSequence[] items = {"price", "rank", "date"};

                 AlertDialog.Builder builder = new AlertDialog.Builder(context1);

                 builder.setTitle("Sort by");

                 builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                     // Click listener
                     public void onClick(DialogInterface dialog, int item) {
                         Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
                        
                         if(items[item]=="none")
                             dialog.dismiss();
                     }
                 });
                 AlertDialog alert = builder.create();
                 //display dialog box
                 alert.show();
             }
      }
      });  
