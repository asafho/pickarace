//
//  listViewController.swift
//  app
//
//  Created by Asaf Horovitz on 12/10/14.
//  Copyright (c) 2014 Asaf Horovitz. All rights reserved.
//


import UIKit

class listViewController: UIViewController, UITableViewDelegate {
    
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
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> TableViewCell {
        let cell: TableViewCell = tableView.dequeueReusableCellWithIdentifier(cellIdentifier) as TableViewCell
        let rowData: NSDictionary = self.tableData[indexPath.row] as NSDictionary
        
        let eventType: String = rowData["type"] as String
        if(eventType==general.MyVariables.contest){
            cell.title?.text = rowData["name"] as? String
            let id: String = rowData["id"] as String
            let date: String = rowData["date"] as String
            let locationObj: NSDictionary = rowData["location"] as NSDictionary
            let city: String = locationObj["city"] as String
            var desc = date+"\n"+city
            cell.subTitle?.text = desc
            cell.subTitle?.textAlignment=NSTextAlignment.Right;
            cell.title?.textAlignment=NSTextAlignment.Right;
            
            
            
            let vendor = rowData["vendor"] as NSDictionary
            var vendorName = vendor["name"] as String
            let urlString = "https://com.pickarace.app.s3.amazonaws.com/"+vendorName+".png"
            println(urlString)
            var image: UIImage? = general.MyVariables.imageCache.valueForKey(vendorName) as? UIImage
            println("image: "+vendorName)
            cell.vendorImage.image = image
    
  
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
        
    }
}
