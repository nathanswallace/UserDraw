// Application to allow a user to drag and drop items on a window 
// Features: Accelerators for key Shortcuts. Using special value 'shortcut' enabling cross platform Windows and Mac
// toolbars, Appending to this comment

package UserDraw;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class UserDraw extends Application{
	double lineinitX;
	double lineinitY; 
	
	int DrawState; // 1 = line, 2 = rect (prob a better way to do this) 
	
	Label response; 
	
	//override the start method
	@Override
	public void start(Stage primaryStage){
		primaryStage.setTitle("Drag and Drop"); 	//Give the stage a title
		BorderPane rootNode = new BorderPane(); // Use a BorderPane for the root node
		Scene myScene = new Scene(rootNode, 1000, 750); //create scene: width, height 
		primaryStage.setScene(myScene); //set the scene on the stage
		
		BorderPane canvasNode = new BorderPane();
		canvasNode.setStyle("-fx-border: 2px solid; -fx-border-color: gray; -fx-background-color: gainsboro");
		Scene CanvasScene = new Scene(canvasNode); 
		

		response = new Label("Drag and Drop"); // create a label that will report the selection
		
		//Create the menu bar
		MenuBar mb = new MenuBar();
		//Create the File Menu with constructor for MenuItems images can change during run-time using setGraphic()
        Menu fileMenu = new Menu("File"); 
        ImageView openIcon = new ImageView("resources/document-open.png");
        MenuItem open = new MenuItem("Open", openIcon);
        ImageView closeIcon = new ImageView("resources/window-close.png");
        MenuItem close = new MenuItem("Close", closeIcon);
        ImageView saveIcon = new ImageView("resources/document-save.png");
        MenuItem save = new MenuItem("Save", saveIcon);
        ImageView exitIcon = new ImageView("resources/application-exit.png");
        MenuItem exit = new MenuItem("Exit", exitIcon);
        fileMenu.getItems().addAll(open, close, save, new SeparatorMenuItem(), exit);
        // Add File menu to the menu bar
        mb.getMenus().add(fileMenu);
        // Add keyboard accelerators for the File menu
        open.setAccelerator(KeyCombination.keyCombination("shortcut+O"));
        close.setAccelerator(KeyCombination.keyCombination("shortcut+C"));
        save.setAccelerator(KeyCombination.keyCombination("shortcut+S"));
        exit.setAccelerator(KeyCombination.keyCombination("shortcut+E"));
        
        
        exit.setOnAction(new EventHandler<ActionEvent>() {
     	    public void handle(ActionEvent t) {
     	        System.exit(0);
     	    }
     	});
        
        // Create the Help menu with about popup window 
        Menu helpMenu = new Menu("Help"); 
	    ImageView aboutIcon = new ImageView("resources/help-contents.png");
	    MenuItem about = new MenuItem("About", aboutIcon);
        about.setOnAction(new EventHandler<ActionEvent>() {
    	    public void handle(ActionEvent a) {
             final Stage dialog = new Stage();
             dialog.initModality(Modality.APPLICATION_MODAL);
             dialog.initOwner(primaryStage);
             VBox dialogVbox = new VBox(20);
             dialogVbox.getChildren().add(new Text("Drag and Drop by Nathan Wallace 2016"));
             Scene dialogScene = new Scene(dialogVbox, 250, 50);
             dialog.setScene(dialogScene);
             dialog.show();
    	    }
        });
        
        helpMenu.getItems().add(about);
        mb.getMenus().add(helpMenu);
        
        // Define a toolbar. First creating toolbar items 
        Button btnTable = new Button("Insert Table", new ImageView("resources/text-x-generic.png"));
        Button btnClear = new Button("Clear Canvas", new ImageView("resources/stock-edit-clear-16.png"));
        Button btnLine  = new Button("Draw Line", new ImageView("resources/line.png"));
        Button btnRect = new Button("Draw Rectangle", new ImageView("resources/rect.png"));
        // Now turn off text in the buttons
        btnTable.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        btnClear.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        btnLine.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        btnRect.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        // Set Tooltips
        btnTable.setTooltip(new Tooltip("Insert Database Table"));
        btnLine.setTooltip(new Tooltip("Draw Line"));
        btnClear.setTooltip(new Tooltip("Clear Canvas"));
        btnRect.setTooltip(new Tooltip("Draw Rectanlge"));
        // Creat the toolbar
        ToolBar tbDraw = new ToolBar(btnTable, btnLine, btnRect, btnClear); 
        // Create a handler for the toolbar buttons.
        
        
        //Drag detection
        btnTable.setOnDragDetected(new EventHandler <MouseEvent>(){
        	public void handle(MouseEvent event) {
        		//drag detected, start drag-and-drop gesture
        		System.out.println("drag detected"); 
                /* allow any transfer mode */
                Dragboard db = btnTable.startDragAndDrop(TransferMode.ANY);
                /* put a string on dragboard */
                ClipboardContent content = new ClipboardContent();
                content.putString(btnTable.getText());
                db.setContent(content);
        	}
        });
        
        
        btnClear.setOnAction(new EventHandler<ActionEvent>(){
        //setOnAction(new EventHandler<ActionEvent>(){
        	public void handle(ActionEvent e){
        		System.out.println("Clear Screen");
        		canvasNode.getChildren().clear();
        	}
        });
        
        canvasNode.setOnDragEntered(new EventHandler<DragEvent>(){
        	public void handle(DragEvent event){
        		System.out.println("Entered");
        		canvasNode.setStyle("-fx-border-width: 5; -fx-border-color: limegreen; -fx-background-color: gainsboro");
        	}
        	
        });
        
        canvasNode.setOnDragExited(new EventHandler<DragEvent>(){
        	public void handle(DragEvent event){
        		System.out.println("Exited");
        		canvasNode.setStyle("-fx-background-color: gainsboro");
        	}
        });
        
        MouseGestures mg = new MouseGestures(); 
        
        canvasNode.setOnDragDropped(new EventHandler<DragEvent>(){
        	public void handle(DragEvent event){
        		System.out.println("Dropped");
        		canvasNode.setStyle("-fx-background-color: gainsboro");
                System.out.println(event.getSceneX());
                System.out.println(event.getSceneY());
                Rectangle r = new Rectangle(); 
                r.setX(event.getSceneX()-100);
                r.setY(event.getSceneY()-30);
                r.setWidth(80);
                r.setHeight(100);
                r.setOpacity(0.7);
                canvasNode.getChildren().add(r);
                mg.makeDraggable(r);
        	}
        });
        
        canvasNode.setOnDragDetected(new EventHandler <MouseEvent>(){
        	public void handle(MouseEvent me){
        		System.out.println("Dragging in window");
        		if (canvasNode.getCursor() == Cursor.CROSSHAIR && DrawState == 1) { //draw line
        			System.out.println("Drawing Temp Line");
//        			Line l = new Line(lineinitX, lineinitY, me.getSceneX()-100, me.getSceneY()-30);
//        			l.setFill(null);
//        			l.setStroke(Color.GREEN);
//        			l.setStrokeWidth(2);
//        			canvasNode.getChildren().add(l);
        		}
        	}
        });
        
        
        canvasNode.setOnMouseDragged(new EventHandler <MouseEvent>(){
        	public void handle(MouseEvent me){
    		System.out.println("Dragging in window for temp line");
    		if (canvasNode.getCursor() == Cursor.CROSSHAIR && DrawState == 1) { //drawing line
    			System.out.println("Drawing Temp Line before drawing new one");
    			Line l = new Line(lineinitX, lineinitY, me.getSceneX()-100, me.getSceneY()-30);
    			l.setFill(null);
    			l.setStroke(Color.GREEN);
    			l.setStrokeWidth(2);
    			canvasNode.getChildren().add(l);
    			//canvasNode.getChildren().remove(canvasNode.getChildren().size()-1); //removes temp line when down
    		}
    	}
        });
        
        canvasNode.setOnMousePressed(new EventHandler <MouseEvent>(){
        	public void handle(MouseEvent me){
        		if (canvasNode.getCursor() == Cursor.CROSSHAIR && DrawState == 1) { //draw line
        			System.out.println("Drawing Line Pressed");
        			lineinitX = me.getSceneX()-100;
        			lineinitY = me.getSceneY()-30;
        			me.consume();
//        			Line l = new Line(); 
//        			l.setStartX(event.getSceneX());
//        			l.setStartY(event.getSceneY());
        		}
        		else if (canvasNode.getCursor() == Cursor.CROSSHAIR && DrawState == 2) { //draw rectangle
        			System.out.println("Drawing Rectangle Pressed");
        			lineinitX = me.getSceneX()-100;
        			lineinitY = me.getSceneY()-30;
        			me.consume();
//        			Line l = new Line(); 
//        			l.setStartX(event.getSceneX());
//        			l.setStartY(event.getSceneY());
        		}
        	}
        });
        

        
        canvasNode.setOnMouseReleased(new EventHandler <MouseEvent>(){
        	public void handle(MouseEvent me){
        		if (canvasNode.getCursor() == Cursor.CROSSHAIR && DrawState == 1){ //line
        			System.out.println("Drawing Line Released");
        			canvasNode.getChildren().remove(canvasNode.getChildren().size()-1); //removes temp line when down
        			Line l = new Line(lineinitX, lineinitY, me.getSceneX()-100, me.getSceneY()-30);
        			l.setFill(null);
        			l.setStroke(Color.BLACK);
        			l.setStrokeWidth(2);
        			canvasNode.getChildren().add(l);
        			mg.makeDraggable(l);
        			DrawState = 0; 
        			canvasNode.setCursor(null);
        		}
        		else if (canvasNode.getCursor() == Cursor.CROSSHAIR && DrawState == 2){ //rect
        			System.out.println("Drawing Rectangle Released");
                    Rectangle r = new Rectangle(); 
                    r.setX(lineinitX);
                    r.setY(lineinitY); 
                    double w = me.getSceneX()-lineinitX-100; 
                    double h = me.getSceneY()-lineinitY-30;
                    System.out.println("Width: "+w);
                    System.out.println("Width: "+h);
                    r.setWidth(w);
                    r.setHeight(h);
                    r.setOpacity(0.9);
                    r.setStrokeWidth(2);
                    canvasNode.getChildren().add(r);
                    mg.makeDraggable(r);
        			DrawState = 0; 
        			canvasNode.setCursor(null);
        		}
        }});
        
        
        canvasNode.setOnDragOver(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* data is dragged over the target */
                System.out.println("onDragOver");
                //canvasScene
                /* accept it only if it is  not dragged from the same node 
                 * and if it has a string data */
                //if (event.getGestureSource() != CanvasScene &&
                  //      event.getDragboard().hasString()) {
                    /* allow for both copying and moving, whatever user chooses */
                    //canvasNode.setStyle("-fx-background-color: red");
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                //}
                
                event.consume();
            }
        });
        
       
        //Used when selected buttons have fired. Still testing... 
        EventHandler<ActionEvent> btnHandler = new EventHandler<ActionEvent>() {
        	public void handle(ActionEvent ae){
        		response.setText(((Button)ae.getTarget()).getText()); //inherits the test from the button definitions 
        		if (ae.getSource() == btnRect) {
        			System.out.println("Draw Rectangle handled");
        			canvasNode.setCursor(Cursor.CROSSHAIR);
        			DrawState = 2; 
        		}
        		if (ae.getSource() == btnLine) {
        			System.out.println("Draw Line handled");
        			canvasNode.setCursor(Cursor.CROSSHAIR); 
        			DrawState = 1; 
        		}
        	}
        };
        // Set the toolbar action event handlers 
        //btnTable.setOnAction(btnHandler);
        btnLine.setOnAction(btnHandler);
        btnRect.setOnAction(btnHandler);
        //btnClear.setOnAction(btnHandler);
 
        
        //Add the menu bar to the top of the border pane and
        //theresponse lable to the center position. 
        rootNode.setTop(mb);
        rootNode.setLeft(response);
        response.setMinWidth(100);
        rootNode.setBottom(tbDraw);
        rootNode.setCenter(canvasNode); //canvas to drop items on top of. 
        
        // Show the stage and its scene
        primaryStage.show();   
        
	}
	
	//Custom Event Handlers for Mousegestures for objects that have already been drawn on window, i.e move. 
	//during object creation call makeDraggable(node) 
	public static class MouseGestures{
		double orgSceneX, orgSceneY; 
		double orgTranslateX, orgTranslateY;
		public void makeDraggable(Node node){

			node.setOnMouseEntered(OnMouseEnteredEventHandler);
			node.setOnMousePressed(OnMousePressedEventHandler);
			node.setOnMouseDragged(OnMouseDraggedEventHandler);
			node.setOnMouseReleased(OnMouseDragReleasedEventHandler);
		}
		
		EventHandler<MouseEvent> OnMouseEnteredEventHandler = new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent t){
				Node p = ((Node) (t.getSource()));
				Node canvas = p.getParent(); 
				System.out.println("Mouse Over");
				p.setCursor(Cursor.HAND); 
			}
		};
		
		EventHandler<MouseEvent> OnMousePressedEventHandler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t){
		        DropShadow ds = new DropShadow();
		        ds.setOffsetY(4.0);
		        ds.setOffsetX(4.0);
		        ds.setColor(Color.LIGHTSKYBLUE);
				orgSceneX = t.getSceneX();
				orgSceneY = t.getSceneY();
				Node p = ((Node) (t.getSource())); 
				p.setEffect(ds);
				orgTranslateX = p.getTranslateX();
				orgTranslateY = p.getTranslateY(); 
			}
		};
		EventHandler<MouseEvent> OnMouseDraggedEventHandler = new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent t){
				double offsetX = t.getSceneX() - orgSceneX;
				double offsetY = t.getSceneY() - orgSceneY;
				double newTranslateX = orgTranslateX + offsetX;
				double newTranslateY = orgTranslateY + offsetY; 
				Node p = ((Node)(t.getSource())); 
				p.setTranslateX(newTranslateX);
				p.setTranslateY(newTranslateY);
			}
		};
		EventHandler<MouseEvent> OnMouseDragReleasedEventHandler = new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent t){
				Node p = ((Node)(t.getSource()));
				//p.getEff
				p.setEffect(null);//removes any effects 
			}
		};
		
	}
	
	// start the JavaFX application by calling launch()
	public static void main(String[] args){
		Application.launch(args);
	}
	
}

//b/c if you dont use it you lose it
