//
//  listViewController.swift
//  app
//
//  Created by Asaf Horovitz on 12/10/14.
//  Copyright (c) 2014 Asaf Horovitz. All rights reserved.
//


import UIKit

class listViewController: UIViewController, UITableViewDataSource, UITableViewDelegate {
    
    let cellIdentifier: String = "contestsCell"

    @IBOutlet var contestsTableView : UITableView?
    var tableData = []
    
    
    override func viewDidLoad() {
        adMob.loadBanners(self)
        filterContests()
        super.viewDidLoad()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
      
    func filterContests(){
        println("filter contests by parameter type: "+general.MyVariables.contest)
      //  var results: NSArray = general.name.jsonURLResult["events"] as NSArray
        var results : NSMutableArray = []
        for contest in general.MyVariables.jsonURLResult["events"] as NSArray{
            let contesttype: String = contest["type"] as String
            if((contest["status"] as String == "active") && (contesttype == general.MyVariables.contest)){
                results.addObject(contest)
              //  (results as NSMutableArray).removeObject(contest);
            }
        }
        
        
        dispatch_async(dispatch_get_main_queue(), {
            self.tableData = results
            self.contestsTableView!.reloadData()
        })
    }
    
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return tableData.count
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell: UITableViewCell = tableView.dequeueReusableCellWithIdentifier(cellIdentifier) as UITableViewCell
        let rowData: NSDictionary = self.tableData[indexPath.row] as NSDictionary
        
        let eventType: String = rowData["type"] as String
        if(eventType==general.MyVariables.contest){
            cell.textLabel?.text = rowData["name"] as? String
            let id: String = rowData["id"] as String
            let date: String = rowData["date"] as String
            let locationObj: NSDictionary = rowData["location"] as NSDictionary
            let city: String = locationObj["city"] as String
            var desc = date+"\n"+city
            cell.detailTextLabel?.text = desc
            cell.detailTextLabel?.textAlignment=NSTextAlignment.Right;
            cell.textLabel?.textAlignment=NSTextAlignment.Right;
   /*   
            let imgURL: NSURL? = NSURL(string: urlString)
            // Download an NSData representation of the image at the URL
        let imgData = NSData(contentsOfURL: imgURL!)
        cell.imageView.image = UIImage(data: imgData!)
     */
        // Get the formatted price string for display in the subtitle
       
        }
        else{
            
        }
        return cell

    }
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        // Get the row data for the selected row
 
        let view = self.storyboard?.instantiateViewControllerWithIdentifier("detailsView") as detailsViewController
        self.navigationController?.pushViewController(view, animated: true)
        general.MyVariables.contestData = self.tableData[indexPath.row] as NSDictionary
        
        /*    var name: String = rowData["name"] as String
        var date: String = rowData["date"] as String
        var details: NSDictionary = rowData["details"] as NSDictionary
        var alert: UIAlertView = UIAlertView()
        alert.title = name
        alert.message = date
        alert.addButtonWithTitle("Ok")
        alert.show()
*/
    }
}
